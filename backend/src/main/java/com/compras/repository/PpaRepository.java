package com.compras.repository;

import com.compras.entity.Ppa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PpaRepository extends JpaRepository<Ppa, Long>, JpaSpecificationExecutor<Ppa> {

    @Query("""
        SELECT p FROM Ppa p
        LEFT JOIN FETCH p.programas pp
        LEFT JOIN FETCH pp.programa
        LEFT JOIN FETCH pp.metas
        WHERE p.id = :id
    """)
    Optional<Ppa> findByIdWithProgramas(@Param("id") Long id);

    List<Ppa> findByStatusOrderByAnoInicioDesc(String status);
}
