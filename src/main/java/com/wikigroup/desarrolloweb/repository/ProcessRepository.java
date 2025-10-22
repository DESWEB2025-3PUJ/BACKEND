package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProcessRepository extends JpaRepository<Process, Long> {
    @Query("SELECT p FROM Process p WHERE p.role.empresa.id = :empresaId")
    List<Process> findByEmpresaId(@Param("empresaId") Long empresaId);
}
