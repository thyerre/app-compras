package com.compras.config;

import com.compras.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataSeeder implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    public DataSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        usuarioRepository.findByEmail(adminEmail).ifPresent(admin -> {
            if (!passwordEncoder.matches(adminPassword, admin.getSenha())) {
                log.info("Fixing admin password hash...");
                admin.setSenha(passwordEncoder.encode(adminPassword));
                usuarioRepository.save(admin);
                log.info("Admin password hash updated successfully.");
            }
        });
    }
}
