package com.compras.service;

import com.compras.dto.LoginRequestDTO;
import com.compras.dto.LoginResponseDTO;
import com.compras.dto.UsuarioDTO;
import com.compras.entity.Usuario;
import com.compras.repository.UsuarioRepository;
import com.compras.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );

            User userDetails = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado"));

            UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                    .id(usuario.getId())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .roles(usuario.getRoles().stream()
                            .map(role -> role.getNome())
                            .collect(Collectors.toList()))
                    .build();

            return LoginResponseDTO.builder()
                    .token(token)
                    .tipo("Bearer")
                    .usuario(usuarioDTO)
                    .build();

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("E-mail ou senha inválidos");
        }
    }
}
