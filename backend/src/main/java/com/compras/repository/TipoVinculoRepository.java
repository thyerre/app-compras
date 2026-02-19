package com.compras.repository;

import com.compras.entity.TipoVinculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoVinculoRepository extends JpaRepository<TipoVinculo, Integer> {

    List<TipoVinculo> findByAtivoTrueOrderByDescricaoAsc();
}
