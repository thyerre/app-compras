package com.compras.repository;

import com.compras.entity.ReceitaOrcamentaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceitaOrcamentariaRepository extends JpaRepository<ReceitaOrcamentaria, Long>,
        JpaSpecificationExecutor<ReceitaOrcamentaria> {
}
