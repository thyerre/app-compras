package com.compras.repository;

import com.compras.entity.BaixaArrecadacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaixaArrecadacaoRepository extends JpaRepository<BaixaArrecadacao, Long>, JpaSpecificationExecutor<BaixaArrecadacao> {

    @Query("""
        SELECT b FROM BaixaArrecadacao b
        LEFT JOIN FETCH b.guiaArrecadacao ga
        LEFT JOIN FETCH ga.contribuinte
        LEFT JOIN FETCH b.agenteArrecadador
        WHERE b.id IN :ids
    """)
    List<BaixaArrecadacao> findByIdsWithAssociations(@Param("ids") List<Long> ids);
}
