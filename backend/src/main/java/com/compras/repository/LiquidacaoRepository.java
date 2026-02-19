package com.compras.repository;

import com.compras.entity.Liquidacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LiquidacaoRepository extends JpaRepository<Liquidacao, Long>,
        JpaSpecificationExecutor<Liquidacao> {

    @Query("""
        SELECT l FROM Liquidacao l
        LEFT JOIN FETCH l.empenho e
        LEFT JOIN FETCH e.fornecedor
        WHERE l.id = :id
    """)
    Optional<Liquidacao> findByIdWithAssociations(@Param("id") Long id);

    @Query("""
        SELECT l FROM Liquidacao l
        LEFT JOIN FETCH l.empenho e
        LEFT JOIN FETCH e.fornecedor
        WHERE l.id IN :ids
    """)
    List<Liquidacao> findByIdsWithAssociations(@Param("ids") List<Long> ids);

    List<Liquidacao> findByEmpenhoId(Long empenhoId);
}
