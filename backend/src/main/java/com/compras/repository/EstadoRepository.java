package com.compras.repository;

import com.compras.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {

    Optional<Estado> findBySigla(String sigla);
}
