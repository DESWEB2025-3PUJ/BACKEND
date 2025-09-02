package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}

