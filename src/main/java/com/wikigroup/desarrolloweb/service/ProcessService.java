package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.model.Process;
import com.wikigroup.desarrolloweb.repository.ProcessRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProcessService {

    private final ProcessRepository processRepository;

    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    public List<Process> findAll() {
        return processRepository.findAll();
    }

    public Optional<Process> findById(Long id) {
        return processRepository.findById(id);
    }

    public Process save(Process process) {
        return processRepository.save(process);
    }

    public void delete(Long id) {
        processRepository.deleteById(id);
    }
}
