package com.compras.repository;

import com.compras.entity.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long>,
        JpaSpecificationExecutor<ContaBancaria> {
}
