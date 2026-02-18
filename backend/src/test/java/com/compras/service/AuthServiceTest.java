package com.compras.service;

import com.compras.dto.LoginRequestDTO;
import com.compras.dto.LoginResponseDTO;
import com.compras.entity.Role;
import com.compras.entity.Usuario;
import com.compras.repository.UsuarioRepository;
import com.compras.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should return token and user data on successful login")
    void shouldReturnTokenOnSuccessfulLogin() {
        LoginRequestDTO request = new LoginRequestDTO("admin@compras.gov.br", "admin123");

        User userDetails = new User(
                "admin@compras.gov.br",
                "hashedpassword",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(userDetails)).thenReturn("generated-jwt-token");

        Role adminRole = Role.builder().id(1L).nome("ADMIN").build();
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("Administrador")
                .email("admin@compras.gov.br")
                .senha("hashedpassword")
                .roles(Set.of(adminRole))
                .build();

        when(usuarioRepository.findByEmail("admin@compras.gov.br")).thenReturn(Optional.of(usuario));

        LoginResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertEquals("generated-jwt-token", response.getToken());
        assertEquals("Bearer", response.getTipo());
        assertEquals("admin@compras.gov.br", response.getUsuario().getEmail());
        assertEquals("Administrador", response.getUsuario().getNome());
        assertTrue(response.getUsuario().getRoles().contains("ADMIN"));
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when credentials are invalid")
    void shouldThrowExceptionWhenCredentialsAreInvalid() {
        LoginRequestDTO request = new LoginRequestDTO("wrong@email.com", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }
}
