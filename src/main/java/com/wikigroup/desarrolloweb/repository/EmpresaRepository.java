package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {}

