package com.compras.repository;

import com.compras.entity.DemonstracaoContabil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DemonstracaoContabilRepository extends JpaRepository<DemonstracaoContabil, Long>,
        JpaSpecificationExecutor<DemonstracaoContabil> {
}
