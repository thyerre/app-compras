package com.compras.repository;

import com.compras.entity.HistoricoLicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoLicitacaoRepository extends JpaRepository<HistoricoLicitacao, Long> {

    List<HistoricoLicitacao> findByFornecedorIdOrderByDataParticipacaoDesc(Long fornecedorId);
}
