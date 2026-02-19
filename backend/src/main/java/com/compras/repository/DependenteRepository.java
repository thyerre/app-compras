package com.compras.repository;

import com.compras.entity.Dependente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DependenteRepository extends JpaRepository<Dependente, Long>, JpaSpecificationExecutor<Dependente> {
}
