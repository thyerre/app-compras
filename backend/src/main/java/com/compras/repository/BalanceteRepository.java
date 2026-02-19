package com.compras.repository;

import com.compras.entity.Balancete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceteRepository extends JpaRepository<Balancete, Long>,
        JpaSpecificationExecutor<Balancete> {
}
