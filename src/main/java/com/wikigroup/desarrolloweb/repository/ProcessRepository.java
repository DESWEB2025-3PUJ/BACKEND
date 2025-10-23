package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.BpmProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProcessRepository extends JpaRepository<BpmProcess, Long> {
    @Query("SELECT p FROM BpmProcess p WHERE p.role.empresa.id = :empresaId")
    List<BpmProcess> findByEmpresaId(@Param("empresaId") Long empresaId);
}