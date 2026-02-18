package com.compras.repository;

import com.compras.entity.TipoFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoFornecedorRepository extends JpaRepository<TipoFornecedor, Integer> {
}
