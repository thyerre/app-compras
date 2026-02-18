package com.compras.repository;

import com.compras.entity.CertidaoFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertidaoFiscalRepository extends JpaRepository<CertidaoFiscal, Long> {

    List<CertidaoFiscal> findByFornecedorId(Long fornecedorId);
}
