package com.wikigroup.desarrolloweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Usuario
 * HU-02: Registro de usuario en empresa
 * HU-03: Inicio de sesión
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * HU-03: Buscar usuario por email para login
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * HU-02: Verificar si existe un usuario con el email dado
     */
    boolean existsByEmail(String email);
    
    /**
     * HU-02: Obtener todos los usuarios de una empresa
     */
    List<Usuario> findByEmpresaId(Long empresaId);
    
    /**
     * HU-02: Contar usuarios por empresa y rol
     * (útil para validar que no se elimine el último administrador)
     */
    long countByEmpresaIdAndRol(Long empresaId, String rol);
    
    /**
     * HU-03: Obtener usuarios por empresa (para acceso restringido)
     */
    List<Usuario> findByEmpresa_Id(Long empresaId);
}

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
