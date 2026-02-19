package com.compras.repository;

import com.compras.entity.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServidorRepository extends JpaRepository<Servidor, Long>, JpaSpecificationExecutor<Servidor> {

    boolean existsByMatricula(String matricula);

    boolean existsByMatriculaAndIdNot(String matricula, Long id);

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    @Query("""
        SELECT s FROM Servidor s
        LEFT JOIN FETCH s.cargo
        LEFT JOIN FETCH s.tipoVinculo
        LEFT JOIN FETCH s.orgao
        LEFT JOIN FETCH s.unidade
        LEFT JOIN FETCH s.usuario
        LEFT JOIN FETCH s.municipio m
        LEFT JOIN FETCH m.estado
        LEFT JOIN FETCH s.estado
        WHERE s.id IN :ids
    """)
    List<Servidor> findByIdsWithAssociations(@Param("ids") List<Long> ids);
}
