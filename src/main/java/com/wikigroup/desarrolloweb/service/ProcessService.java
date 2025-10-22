package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.model.Process;
import com.wikigroup.desarrolloweb.repository.ProcessRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessService {

    private final ProcessRepository repository;

    public ProcessService(ProcessRepository repository) {
        this.repository = repository;
    }

    public List<Process> findAll() {
        return repository.findAll();
    }

    public Process findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Process not found with id " + id));
    }

    public Process save(Process process) {
        return repository.save(process);
    }

    public List<Process> findByEmpresaId(Long empresaId) {
        return repository.findByEmpresaId(empresaId);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Process not found with id " + id);
        }
        repository.deleteById(id);
    }
}

