package com.compras.repository;

import com.compras.entity.DotacaoOrcamentaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DotacaoOrcamentariaRepository extends JpaRepository<DotacaoOrcamentaria, Long>,
        JpaSpecificationExecutor<DotacaoOrcamentaria> {

    @Query("""
        SELECT d FROM DotacaoOrcamentaria d
        LEFT JOIN FETCH d.loa
        LEFT JOIN FETCH d.orgao
        LEFT JOIN FETCH d.unidade
        LEFT JOIN FETCH d.funcao
        LEFT JOIN FETCH d.subfuncao
        LEFT JOIN FETCH d.programa
        LEFT JOIN FETCH d.acao
        LEFT JOIN FETCH d.naturezaDespesa
        LEFT JOIN FETCH d.fonteRecurso
        WHERE d.id = :id
    """)
    Optional<DotacaoOrcamentaria> findByIdWithAssociations(@Param("id") Long id);

    @Query("""
        SELECT d FROM DotacaoOrcamentaria d
        LEFT JOIN FETCH d.loa
        LEFT JOIN FETCH d.orgao
        LEFT JOIN FETCH d.unidade
        LEFT JOIN FETCH d.funcao
        LEFT JOIN FETCH d.subfuncao
        LEFT JOIN FETCH d.programa
        LEFT JOIN FETCH d.acao
        LEFT JOIN FETCH d.naturezaDespesa
        LEFT JOIN FETCH d.fonteRecurso
        WHERE d.id IN :ids
    """)
    List<DotacaoOrcamentaria> findByIdsWithAssociations(@Param("ids") List<Long> ids);

    List<DotacaoOrcamentaria> findByLoaId(Long loaId);
}
