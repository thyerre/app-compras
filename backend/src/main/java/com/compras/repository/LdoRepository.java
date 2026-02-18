package com.compras.repository;

import com.compras.entity.Ldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LdoRepository extends JpaRepository<Ldo, Long>, JpaSpecificationExecutor<Ldo> {

    @Query("""
        SELECT l FROM Ldo l
        LEFT JOIN FETCH l.ppa
        LEFT JOIN FETCH l.prioridades p
        LEFT JOIN FETCH p.programa
        LEFT JOIN FETCH p.acao
        WHERE l.id = :id
    """)
    Optional<Ldo> findByIdWithPrioridades(@Param("id") Long id);
}
