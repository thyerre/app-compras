package com.compras.repository;

import com.compras.entity.AgenteArrecadador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgenteArrecadadorRepository extends JpaRepository<AgenteArrecadador, Integer> {

    List<AgenteArrecadador> findByAtivoTrueOrderByDescricaoAsc();
}
