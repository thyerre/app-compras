package com.compras.repository;

import com.compras.entity.RestosAPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestosAPagarRepository extends JpaRepository<RestosAPagar, Long>,
        JpaSpecificationExecutor<RestosAPagar> {

    @Query("""
        SELECT r FROM RestosAPagar r
        LEFT JOIN FETCH r.empenho e
        LEFT JOIN FETCH e.fornecedor
        WHERE r.id = :id
    """)
    Optional<RestosAPagar> findByIdWithAssociations(@Param("id") Long id);

    @Query("""
        SELECT r FROM RestosAPagar r
        LEFT JOIN FETCH r.empenho e
        LEFT JOIN FETCH e.fornecedor
        WHERE r.id IN :ids
    """)
    List<RestosAPagar> findByIdsWithAssociations(@Param("ids") List<Long> ids);
}
