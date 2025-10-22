package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.model.Gateway;
import com.wikigroup.desarrolloweb.repository.GatewayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatewayService {

    private final GatewayRepository repository;

    public GatewayService(GatewayRepository repository) {
        this.repository = repository;
    }

    public List<Gateway> findAll() {
        return repository.findAll();
    }

    public Gateway findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gateway not found with id " + id));
    }

    public Gateway save(Gateway gateway) {
        return repository.save(gateway);
    }

    public List<Gateway> findByProcessId(Long processId) {
        return repository.findByProcessId(processId);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Gateway not found with id " + id);
        }
        repository.deleteById(id);
    }
}
