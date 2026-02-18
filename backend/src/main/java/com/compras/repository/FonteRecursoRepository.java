package com.compras.repository;

import com.compras.entity.FonteRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FonteRecursoRepository extends JpaRepository<FonteRecurso, Integer> {
}
