package com.compras.repository;

import com.compras.entity.ReceitaPrevista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceitaPrevistaRepository extends JpaRepository<ReceitaPrevista, Long>,
        JpaSpecificationExecutor<ReceitaPrevista> {

    List<ReceitaPrevista> findByLoaId(Long loaId);
}
