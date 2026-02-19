package com.compras.repository;

import com.compras.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>,
        JpaSpecificationExecutor<Pagamento> {

    @Query("""
        SELECT p FROM Pagamento p
        LEFT JOIN FETCH p.liquidacao l
        LEFT JOIN FETCH l.empenho e
        LEFT JOIN FETCH e.fornecedor
        LEFT JOIN FETCH p.contaBancaria
        WHERE p.id = :id
    """)
    Optional<Pagamento> findByIdWithAssociations(@Param("id") Long id);

    @Query("""
        SELECT p FROM Pagamento p
        LEFT JOIN FETCH p.liquidacao l
        LEFT JOIN FETCH l.empenho e
        LEFT JOIN FETCH e.fornecedor
        LEFT JOIN FETCH p.contaBancaria
        WHERE p.id IN :ids
    """)
    List<Pagamento> findByIdsWithAssociations(@Param("ids") List<Long> ids);
}
