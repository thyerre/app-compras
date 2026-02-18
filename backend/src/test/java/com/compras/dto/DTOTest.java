package com.compras.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DTOTest {

    @Test
    @DisplayName("Should create LoginRequestDTO with correct values")
    void shouldCreateLoginRequestDTO() {
        LoginRequestDTO dto = new LoginRequestDTO("admin@compras.gov.br", "admin123");

        assertEquals("admin@compras.gov.br", dto.getEmail());
        assertEquals("admin123", dto.getSenha());
    }

    @Test
    @DisplayName("Should create LoginResponseDTO using builder")
    void shouldCreateLoginResponseDTO() {
        UsuarioDTO usuario = UsuarioDTO.builder()
                .id(1L)
                .nome("Admin")
                .email("admin@compras.gov.br")
                .roles(List.of("ADMIN"))
                .build();

        LoginResponseDTO response = LoginResponseDTO.builder()
                .token("jwt-token")
                .tipo("Bearer")
                .usuario(usuario)
                .build();

        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getTipo());
        assertNotNull(response.getUsuario());
        assertEquals("Admin", response.getUsuario().getNome());
    }

    @Test
    @DisplayName("Should create MenuDTO with children")
    void shouldCreateMenuDTOWithChildren() {
        MenuDTO child = MenuDTO.builder()
                .id(2L)
                .label("Requests")
                .icone("description")
                .rota("/requests")
                .ordem(1)
                .children(List.of())
                .build();

        MenuDTO parent = MenuDTO.builder()
                .id(1L)
                .label("Procurement")
                .icone("shopping_cart")
                .rota(null)
                .ordem(1)
                .children(List.of(child))
                .build();

        assertEquals("Procurement", parent.getLabel());
        assertEquals(1, parent.getChildren().size());
        assertEquals("Requests", parent.getChildren().get(0).getLabel());
    }

    @Test
    @DisplayName("Should create UsuarioDTO with roles")
    void shouldCreateUsuarioDTOWithRoles() {
        UsuarioDTO dto = UsuarioDTO.builder()
                .id(1L)
                .nome("Test User")
                .email("test@test.com")
                .roles(List.of("ADMIN", "USUARIO"))
                .build();

        assertEquals(2, dto.getRoles().size());
        assertTrue(dto.getRoles().contains("ADMIN"));
    }
}
