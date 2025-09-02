package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository) {
        this.repository = repository;
    }

    public List<Empresa> findAll() {
        return repository.findAll();
    }

    public Empresa findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa not found with id " + id));
    }

    public Empresa save(Empresa empresa) {
        return repository.save(empresa);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Empresa not found with id " + id);
        }
        repository.deleteById(id);
    }
}

