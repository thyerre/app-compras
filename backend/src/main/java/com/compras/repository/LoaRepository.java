package com.compras.repository;

import com.compras.entity.Loa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaRepository extends JpaRepository<Loa, Long>, JpaSpecificationExecutor<Loa> {
}
