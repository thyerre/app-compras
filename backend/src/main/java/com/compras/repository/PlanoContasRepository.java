package com.compras.repository;

import com.compras.entity.PlanoContas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanoContasRepository extends JpaRepository<PlanoContas, Long>,
        JpaSpecificationExecutor<PlanoContas> {

    boolean existsByCodigo(String codigo);
    boolean existsByCodigoAndIdNot(String codigo, Long id);
    Optional<PlanoContas> findByCodigo(String codigo);
}
