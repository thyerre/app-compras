package com.compras.repository;

import com.compras.entity.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Integer>, JpaSpecificationExecutor<Unidade> {

    List<Unidade> findByOrgaoIdOrderByNomeAsc(Integer orgaoId);

    List<Unidade> findByAtivoTrueOrderByNomeAsc();

    boolean existsByCodigoAndOrgaoId(String codigo, Integer orgaoId);

    boolean existsByCodigoAndOrgaoIdAndIdNot(String codigo, Integer orgaoId, Integer id);
}
