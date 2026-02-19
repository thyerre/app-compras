package com.compras.repository;

import com.compras.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer>, JpaSpecificationExecutor<Cargo> {

    List<Cargo> findByAtivoTrueOrderByDescricaoAsc();

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Integer id);
}
