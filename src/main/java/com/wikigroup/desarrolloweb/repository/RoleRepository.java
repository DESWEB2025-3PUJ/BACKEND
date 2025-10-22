package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByEmpresaId(Long empresaId);
}

