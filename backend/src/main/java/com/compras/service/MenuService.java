package com.compras.service;

import com.compras.dto.MenuDTO;
import com.compras.entity.Menu;
import com.compras.entity.Usuario;
import com.compras.repository.MenuRepository;
import com.compras.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<MenuDTO> getMenusByUsuarioEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        List<String> roleNames = usuario.getRoles().stream()
                .map(role -> role.getNome())
                .collect(Collectors.toList());

        List<Menu> menus = menuRepository.findMenusByRoleNames(roleNames);

        return menus.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MenuDTO toDTO(Menu menu) {
        return MenuDTO.builder()
                .id(menu.getId())
                .label(menu.getLabel())
                .icone(menu.getIcone())
                .rota(menu.getRota())
                .ordem(menu.getOrdem())
                .children(menu.getChildren() != null
                        ? menu.getChildren().stream()
                            .filter(Menu::getAtivo)
                            .map(this::toDTO)
                            .collect(Collectors.toList())
                        : List.of())
                .build();
    }
}
