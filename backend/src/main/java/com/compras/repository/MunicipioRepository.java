package com.compras.repository;

import com.compras.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {

    List<Municipio> findByEstadoIdOrderByNome(Integer estadoId);

    List<Municipio> findByNomeContainingIgnoreCaseOrderByNome(String nome);
}
