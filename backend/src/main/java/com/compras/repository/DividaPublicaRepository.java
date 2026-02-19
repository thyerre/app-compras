package com.compras.repository;

import com.compras.entity.DividaPublica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DividaPublicaRepository extends JpaRepository<DividaPublica, Long>,
        JpaSpecificationExecutor<DividaPublica> {
}
