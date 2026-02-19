package com.compras.repository;

import com.compras.entity.Licenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LicencaRepository extends JpaRepository<Licenca, Long>, JpaSpecificationExecutor<Licenca> {
}
