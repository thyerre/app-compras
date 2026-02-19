package com.compras.repository;

import com.compras.entity.BemPatrimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BemPatrimonialRepository extends JpaRepository<BemPatrimonial, Long>,
        JpaSpecificationExecutor<BemPatrimonial> {

    boolean existsByNumeroPatrimonio(String numeroPatrimonio);
    boolean existsByNumeroPatrimonioAndIdNot(String numeroPatrimonio, Long id);
}
