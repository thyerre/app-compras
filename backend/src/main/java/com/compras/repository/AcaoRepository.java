package com.compras.repository;

import com.compras.entity.Acao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcaoRepository extends JpaRepository<Acao, Integer>, JpaSpecificationExecutor<Acao> {

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Integer id);

    List<Acao> findByAtivoTrueOrderByNomeAsc();

    @Query("""
        SELECT a FROM Acao a
        LEFT JOIN FETCH a.funcao
        LEFT JOIN FETCH a.subfuncao
        WHERE a.id IN :ids
    """)
    List<Acao> findByIdsWithAssociations(@Param("ids") List<Integer> ids);
}
