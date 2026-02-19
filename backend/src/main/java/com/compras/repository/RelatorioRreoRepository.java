package com.compras.repository;

import com.compras.entity.RelatorioRreo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatorioRreoRepository extends JpaRepository<RelatorioRreo, Long>,
        JpaSpecificationExecutor<RelatorioRreo> {
}
