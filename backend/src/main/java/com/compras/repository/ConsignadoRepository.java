package com.compras.repository;

import com.compras.entity.Consignado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsignadoRepository extends JpaRepository<Consignado, Long>, JpaSpecificationExecutor<Consignado> {
}
