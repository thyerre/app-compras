package com.compras.repository;

import com.compras.entity.Contribuinte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContribuinteRepository extends JpaRepository<Contribuinte, Long>, JpaSpecificationExecutor<Contribuinte> {

    boolean existsByCpfCnpj(String cpfCnpj);

    boolean existsByCpfCnpjAndIdNot(String cpfCnpj, Long id);

    @Query("""
        SELECT c FROM Contribuinte c
        LEFT JOIN FETCH c.municipio m
        LEFT JOIN FETCH m.estado
        LEFT JOIN FETCH c.estado
        WHERE c.id IN :ids
    """)
    List<Contribuinte> findByIdsWithAssociations(@Param("ids") List<Long> ids);
}
