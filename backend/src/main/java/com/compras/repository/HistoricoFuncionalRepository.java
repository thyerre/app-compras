package com.compras.repository;

import com.compras.entity.HistoricoFuncional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoFuncionalRepository extends JpaRepository<HistoricoFuncional, Long>, JpaSpecificationExecutor<HistoricoFuncional> {
}
