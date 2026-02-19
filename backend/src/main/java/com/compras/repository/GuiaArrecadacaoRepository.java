package com.compras.repository;

import com.compras.entity.GuiaArrecadacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuiaArrecadacaoRepository extends JpaRepository<GuiaArrecadacao, Long>, JpaSpecificationExecutor<GuiaArrecadacao> {

    @Query("""
        SELECT g FROM GuiaArrecadacao g
        LEFT JOIN FETCH g.lancamentoTributario
        LEFT JOIN FETCH g.contribuinte
        WHERE g.id IN :ids
    """)
    List<GuiaArrecadacao> findByIdsWithAssociations(@Param("ids") List<Long> ids);
}
