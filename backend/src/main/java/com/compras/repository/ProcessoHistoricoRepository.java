package com.compras.repository;

import com.compras.entity.ProcessoHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessoHistoricoRepository extends JpaRepository<ProcessoHistorico, Long> {

    List<ProcessoHistorico> findByProcessoIdOrderByDataRegistroDesc(Long processoId);
}
