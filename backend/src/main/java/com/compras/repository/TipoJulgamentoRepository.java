package com.compras.repository;

import com.compras.entity.TipoJulgamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoJulgamentoRepository extends JpaRepository<TipoJulgamento, Integer> {
}
