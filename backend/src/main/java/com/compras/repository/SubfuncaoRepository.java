package com.compras.repository;

import com.compras.entity.Subfuncao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubfuncaoRepository extends JpaRepository<Subfuncao, Integer> {

    List<Subfuncao> findByFuncaoIdOrderByNomeAsc(Integer funcaoId);
}
