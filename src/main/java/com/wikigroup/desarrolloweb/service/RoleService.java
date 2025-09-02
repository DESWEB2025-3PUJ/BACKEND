package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.model.Role;
import com.wikigroup.desarrolloweb.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> findAll() {
        return repository.findAll();
    }

    public Role findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id " + id));
    }

    public Role save(Role role) {
        return repository.save(role);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Role not found with id " + id);
        }
        repository.deleteById(id);
    }
}
