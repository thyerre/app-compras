package com.compras.controller;

import com.compras.dto.MenuDTO;
import com.compras.security.JwtService;
import com.compras.service.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuService menuService;

    @Test
    @DisplayName("Should return 401 when user is not authenticated")
    void shouldReturn401WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return menus for an authenticated user")
    @WithMockUser(username = "admin@compras.gov.br", roles = {"ADMIN"})
    void shouldReturnMenusForAuthenticatedUser() throws Exception {
        List<MenuDTO> menus = List.of(
                MenuDTO.builder()
                        .id(1L)
                        .label("Dashboard")
                        .icone("dashboard")
                        .rota("/dashboard")
                        .ordem(1)
                        .children(List.of())
                        .build()
        );

        org.mockito.Mockito.when(menuService.getMenusByUsuarioEmail("admin@compras.gov.br"))
                .thenReturn(menus);

        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("Dashboard"))
                .andExpect(jsonPath("$[0].rota").value("/dashboard"));
    }

    @Test
    @DisplayName("Should return an empty list when user has no menus")
    @WithMockUser(username = "user@compras.gov.br", roles = {"USUARIO"})
    void shouldReturnEmptyListWhenUserHasNoMenus() throws Exception {
        org.mockito.Mockito.when(menuService.getMenusByUsuarioEmail("user@compras.gov.br"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
