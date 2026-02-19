package com.compras.repository;

import com.compras.entity.CreditoAdicional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditoAdicionalRepository extends JpaRepository<CreditoAdicional, Long>,
        JpaSpecificationExecutor<CreditoAdicional> {
}
