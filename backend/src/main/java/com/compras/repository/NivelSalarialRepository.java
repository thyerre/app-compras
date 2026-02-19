package com.compras.repository;

import com.compras.entity.NivelSalarial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NivelSalarialRepository extends JpaRepository<NivelSalarial, Integer>, JpaSpecificationExecutor<NivelSalarial> {

    List<NivelSalarial> findByCargoIdAndAtivoTrueOrderByNivelAsc(Integer cargoId);
}
