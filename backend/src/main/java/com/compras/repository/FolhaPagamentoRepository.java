package com.compras.repository;

import com.compras.entity.FolhaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FolhaPagamentoRepository extends JpaRepository<FolhaPagamento, Long>, JpaSpecificationExecutor<FolhaPagamento> {
}
