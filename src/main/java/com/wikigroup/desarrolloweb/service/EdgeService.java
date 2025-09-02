package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.model.Edge;
import com.wikigroup.desarrolloweb.repository.EdgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EdgeService {

    private final EdgeRepository repository;

    public EdgeService(EdgeRepository repository) {
        this.repository = repository;
    }

    public List<Edge> findAll() {
        return repository.findAll();
    }

    public Edge findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Edge not found with id " + id));
    }

    public Edge save(Edge edge) {
        return repository.save(edge);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Edge not found with id " + id);
        }
        repository.deleteById(id);
    }
}


