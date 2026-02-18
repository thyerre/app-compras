package com.compras.security;

import com.compras.entity.Role;
import com.compras.entity.Usuario;
import com.compras.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    @DisplayName("Should load user by email and return UserDetails")
    void shouldLoadUserByEmail() {
        Role adminRole = Role.builder().id(1L).nome("ADMIN").build();
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("Admin")
                .email("admin@compras.gov.br")
                .senha("$2a$12$hashedpassword")
                .ativo(true)
                .roles(Set.of(adminRole))
                .build();

        when(usuarioRepository.findByEmail("admin@compras.gov.br")).thenReturn(Optional.of(usuario));

        UserDetails result = userDetailsService.loadUserByUsername("admin@compras.gov.br");

        assertNotNull(result);
        assertEquals("admin@compras.gov.br", result.getUsername());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user is not found")
    void shouldThrowExceptionWhenUserNotFound() {
        when(usuarioRepository.findByEmail("nonexistent@compras.gov.br")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nonexistent@compras.gov.br"));
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user is inactive")
    void shouldThrowExceptionWhenUserIsInactive() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("Inactive User")
                .email("inactive@compras.gov.br")
                .senha("$2a$12$hashedpassword")
                .ativo(false)
                .roles(Set.of())
                .build();

        when(usuarioRepository.findByEmail("inactive@compras.gov.br")).thenReturn(Optional.of(usuario));

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("inactive@compras.gov.br"));
    }
}
