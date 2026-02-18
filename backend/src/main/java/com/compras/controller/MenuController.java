package com.compras.controller;

import com.compras.dto.MenuDTO;
import com.compras.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuDTO>> getMenus(Authentication authentication) {
        List<MenuDTO> menus = menuService.getMenusByUsuarioEmail(authentication.getName());
        return ResponseEntity.ok(menus);
    }
}
