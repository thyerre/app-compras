package com.compras.repository;

import com.compras.entity.RelatorioRgf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatorioRgfRepository extends JpaRepository<RelatorioRgf, Long>,
        JpaSpecificationExecutor<RelatorioRgf> {
}
