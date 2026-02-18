package com.compras.repository;

import com.compras.entity.NaturezaDespesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaturezaDespesaRepository extends JpaRepository<NaturezaDespesa, Integer> {
}
