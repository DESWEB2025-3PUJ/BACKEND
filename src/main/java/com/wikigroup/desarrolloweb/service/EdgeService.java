package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.model.Edge;
import com.wikigroup.desarrolloweb.repository.EdgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EdgeService {

    private final EdgeRepository edgeRepository;

    public EdgeService(EdgeRepository edgeRepository) {
        this.edgeRepository = edgeRepository;
    }

    public List<Edge> findAll() {
        return edgeRepository.findAll();
    }

    public Optional<Edge> findById(Long id) {
        return edgeRepository.findById(id);
    }

    public Edge save(Edge edge) {
        return edgeRepository.save(edge);
    }

    public void delete(Long id) {
        edgeRepository.deleteById(id);
    }
}

