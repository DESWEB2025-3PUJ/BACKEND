package com.wikigroup.desarrolloweb.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wikigroup.desarrolloweb.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByEmpresaId(Long empresaId);
}