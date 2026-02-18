package com.compras.controller;

import com.compras.dto.LoginRequestDTO;
import com.compras.dto.LoginResponseDTO;
import com.compras.dto.UsuarioDTO;
import com.compras.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    @DisplayName("Should return 200 and token when login is successful")
    void shouldReturnTokenWhenLoginIsSuccessful() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("admin@compras.gov.br", "admin123");

        LoginResponseDTO response = LoginResponseDTO.builder()
                .token("jwt-token-here")
                .tipo("Bearer")
                .usuario(UsuarioDTO.builder()
                        .id(1L)
                        .nome("Administrador")
                        .email("admin@compras.gov.br")
                        .roles(List.of("ADMIN"))
                        .build())
                .build();

        org.mockito.Mockito.when(authService.login(org.mockito.ArgumentMatchers.any(LoginRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-here"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.usuario.email").value("admin@compras.gov.br"));
    }

    @Test
    @DisplayName("Should return 401 when credentials are invalid")
    void shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("wrong@email.com", "wrongpassword");

        org.mockito.Mockito.when(authService.login(org.mockito.ArgumentMatchers.any(LoginRequestDTO.class)))
                .thenThrow(new BadCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 400 when email is blank")
    void shouldReturn400WhenEmailIsBlank() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("", "admin123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when password is blank")
    void shouldReturn400WhenPasswordIsBlank() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("admin@compras.gov.br", "");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
