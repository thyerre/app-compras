package com.compras.repository;

import com.compras.entity.Orgao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrgaoRepository extends JpaRepository<Orgao, Integer>, JpaSpecificationExecutor<Orgao> {

    Optional<Orgao> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Integer id);

    List<Orgao> findByAtivoTrueOrderByNomeAsc();
}
