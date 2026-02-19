package com.compras.repository;

import com.compras.entity.LancamentoTributario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LancamentoTributarioRepository extends JpaRepository<LancamentoTributario, Long>, JpaSpecificationExecutor<LancamentoTributario> {

    @Query("""
        SELECT lt FROM LancamentoTributario lt
        LEFT JOIN FETCH lt.contribuinte
        LEFT JOIN FETCH lt.tributo
        LEFT JOIN FETCH lt.imovel
        LEFT JOIN FETCH lt.atividadeEconomica
        WHERE lt.id IN :ids
    """)
    List<LancamentoTributario> findByIdsWithAssociations(@Param("ids") List<Long> ids);
}
