package com.compras.repository;

import com.compras.entity.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Integer>, JpaSpecificationExecutor<Programa> {

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Integer id);

    List<Programa> findByAtivoTrueOrderByNomeAsc();
}
