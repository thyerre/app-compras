package com.compras.repository;

import com.compras.entity.ModalidadeLicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModalidadeLicitacaoRepository extends JpaRepository<ModalidadeLicitacao, Integer> {
}
