package com.compras.repository;

import com.compras.entity.EventoFolha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoFolhaRepository extends JpaRepository<EventoFolha, Integer>, JpaSpecificationExecutor<EventoFolha> {

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Integer id);
}
