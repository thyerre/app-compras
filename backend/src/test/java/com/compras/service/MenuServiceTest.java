package com.compras.service;

import com.compras.dto.MenuDTO;
import com.compras.entity.Menu;
import com.compras.entity.Role;
import com.compras.entity.Usuario;
import com.compras.repository.MenuRepository;
import com.compras.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("Should return menu tree for a given user email")
    void shouldReturnMenuTreeForUserEmail() {
        Role adminRole = Role.builder().id(1L).nome("ADMIN").build();
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("Admin")
                .email("admin@compras.gov.br")
                .roles(Set.of(adminRole))
                .build();

        Menu dashboardMenu = Menu.builder()
                .id(1L)
                .label("Dashboard")
                .icone("dashboard")
                .rota("/dashboard")
                .ordem(1)
                .ativo(true)
                .build();

        when(usuarioRepository.findByEmail("admin@compras.gov.br")).thenReturn(Optional.of(usuario));
        when(menuRepository.findMenusByRoleNames(List.of("ADMIN"))).thenReturn(List.of(dashboardMenu));

        List<MenuDTO> result = menuService.getMenusByUsuarioEmail("admin@compras.gov.br");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Dashboard", result.get(0).getLabel());
        assertEquals("/dashboard", result.get(0).getRota());
    }

    @Test
    @DisplayName("Should return an empty list when user has no menus")
    void shouldReturnEmptyListWhenUserHasNoMenus() {
        Role userRole = Role.builder().id(2L).nome("USUARIO").build();
        Usuario usuario = Usuario.builder()
                .id(2L)
                .nome("User")
                .email("user@compras.gov.br")
                .roles(Set.of(userRole))
                .build();

        when(usuarioRepository.findByEmail("user@compras.gov.br")).thenReturn(Optional.of(usuario));
        when(menuRepository.findMenusByRoleNames(List.of("USUARIO"))).thenReturn(List.of());

        List<MenuDTO> result = menuService.getMenusByUsuarioEmail("user@compras.gov.br");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist")
    void shouldThrowExceptionWhenUserDoesNotExist() {
        when(usuarioRepository.findByEmail("nonexistent@compras.gov.br")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> menuService.getMenusByUsuarioEmail("nonexistent@compras.gov.br"));
    }
}
