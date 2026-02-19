package com.compras.repository;

import com.compras.entity.Ferias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FeriasRepository extends JpaRepository<Ferias, Long>, JpaSpecificationExecutor<Ferias> {
}
