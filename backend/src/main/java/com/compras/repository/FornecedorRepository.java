package com.compras.repository;

import com.compras.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long>, JpaSpecificationExecutor<Fornecedor> {

    boolean existsByCnpjCpf(String cnpjCpf);

    boolean existsByCnpjCpfAndIdNot(String cnpjCpf, Long id);

    @Query("""
        SELECT f FROM Fornecedor f
        LEFT JOIN FETCH f.tipoFornecedor
        LEFT JOIN FETCH f.classificacao
        LEFT JOIN FETCH f.municipio m
        LEFT JOIN FETCH m.estado
        LEFT JOIN FETCH f.estado
        WHERE f.id IN :ids
    """)
    List<Fornecedor> findByIdsWithAssociations(@Param("ids") List<Long> ids);
}
