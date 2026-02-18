package com.compras.repository;

import com.compras.entity.ProcessoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessoCompraRepository extends JpaRepository<ProcessoCompra, Long>,
        JpaSpecificationExecutor<ProcessoCompra> {

    boolean existsByNumeroProcesso(String numeroProcesso);

    boolean existsByNumeroProcessoAndIdNot(String numeroProcesso, Long id);

    @Query("""
        SELECT p FROM ProcessoCompra p
        LEFT JOIN FETCH p.modalidade
        LEFT JOIN FETCH p.tipoJulgamento
        LEFT JOIN FETCH p.status
        LEFT JOIN FETCH p.orgao
        LEFT JOIN FETCH p.unidade
        LEFT JOIN FETCH p.dotacao
        LEFT JOIN FETCH p.itens i
        LEFT JOIN FETCH i.fornecedorVencedor
        LEFT JOIN FETCH p.participantes pp
        LEFT JOIN FETCH pp.fornecedor
        LEFT JOIN FETCH p.documentos
        WHERE p.id = :id
    """)
    Optional<ProcessoCompra> findByIdWithAssociations(@Param("id") Long id);

    @Query("""
        SELECT p FROM ProcessoCompra p
        LEFT JOIN FETCH p.modalidade
        LEFT JOIN FETCH p.status
        LEFT JOIN FETCH p.orgao
        WHERE p.id IN :ids
    """)
    List<ProcessoCompra> findByIdsWithAssociations(@Param("ids") List<Long> ids);
}
