package com.compras.repository;

import com.compras.entity.StatusProcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusProcessoRepository extends JpaRepository<StatusProcesso, Integer> {

    Optional<StatusProcesso> findByNome(String nome);
}
