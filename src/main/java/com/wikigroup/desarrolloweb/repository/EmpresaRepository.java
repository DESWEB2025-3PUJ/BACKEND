package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad Empresa
 * HU-01: Registro de empresa
 */
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    /**
     * HU-01: Verifica si existe una empresa con el NIT dado
     */
    boolean existsByNit(String nit);
    
    /**
     * HU-01: Busca una empresa por NIT
     */
    Optional<Empresa> findByNit(String nit);
    
    /**
     * HU-01: Verifica si existe una empresa con el correo de contacto dado
     */
    boolean existsByCorreoContacto(String correoContacto);
}

