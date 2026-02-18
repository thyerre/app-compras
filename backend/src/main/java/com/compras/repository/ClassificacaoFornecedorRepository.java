package com.compras.repository;

import com.compras.entity.ClassificacaoFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassificacaoFornecedorRepository extends JpaRepository<ClassificacaoFornecedor, Integer> {
}
