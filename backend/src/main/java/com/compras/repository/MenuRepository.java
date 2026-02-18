package com.compras.repository;

import com.compras.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("""
        SELECT DISTINCT m FROM Menu m
        JOIN m.roles r
        WHERE r.nome IN :roleNames
        AND m.ativo = true
        AND m.parent IS NULL
        ORDER BY m.ordem
    """)
    List<Menu> findMenusByRoleNames(@Param("roleNames") List<String> roleNames);

    List<Menu> findByParentIdAndAtivoTrueOrderByOrdemAsc(Long parentId);
}
