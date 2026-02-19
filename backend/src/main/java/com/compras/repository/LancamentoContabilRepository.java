package com.compras.repository;

import com.compras.entity.LancamentoContabil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LancamentoContabilRepository extends JpaRepository<LancamentoContabil, Long>,
        JpaSpecificationExecutor<LancamentoContabil> {

    @Query("""
        SELECT l FROM LancamentoContabil l
        LEFT JOIN FETCH l.itens i
        LEFT JOIN FETCH i.planoConta
        WHERE l.id = :id
    """)
    Optional<LancamentoContabil> findByIdWithItens(@Param("id") Long id);
}
