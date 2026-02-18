package com.compras.repository;

import com.compras.entity.Empenho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpenhoRepository extends JpaRepository<Empenho, Long>,
        JpaSpecificationExecutor<Empenho> {

    boolean existsByNumeroEmpenho(String numeroEmpenho);

    boolean existsByNumeroEmpenhoAndIdNot(String numeroEmpenho, Long id);

    @Query("""
        SELECT e FROM Empenho e
        LEFT JOIN FETCH e.processo p
        LEFT JOIN FETCH p.modalidade
        LEFT JOIN FETCH e.fornecedor f
        LEFT JOIN FETCH f.tipoFornecedor
        LEFT JOIN FETCH e.dotacao d
        LEFT JOIN FETCH d.loa
        WHERE e.id = :id
    """)
    Optional<Empenho> findByIdWithAssociations(@Param("id") Long id);

    @Query("""
        SELECT e FROM Empenho e
        LEFT JOIN FETCH e.processo
        LEFT JOIN FETCH e.fornecedor
        WHERE e.id IN :ids
    """)
    List<Empenho> findByIdsWithAssociations(@Param("ids") List<Long> ids);

    List<Empenho> findByProcessoId(Long processoId);
}
