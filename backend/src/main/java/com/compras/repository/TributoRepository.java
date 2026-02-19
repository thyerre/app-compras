package com.compras.repository;

import com.compras.entity.Tributo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TributoRepository extends JpaRepository<Tributo, Integer> {

    List<Tributo> findByAtivoTrueOrderByCodigoAsc();
}
