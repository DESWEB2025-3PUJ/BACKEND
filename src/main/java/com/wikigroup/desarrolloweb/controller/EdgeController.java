package com.wikigroup.desarrolloweb.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wikigroup.desarrolloweb.model.Edge;
import com.wikigroup.desarrolloweb.service.EdgeService;

@RestController
@RequestMapping("/api/edges")
public class EdgeController {

    private final EdgeService edgeService;

    public EdgeController(EdgeService edgeService) {
        this.edgeService = edgeService;
    }

    @GetMapping
    public List<Edge> getAll() {
        return edgeService.findAll();
    }

    @GetMapping("/{id}")
    public Edge getById(@PathVariable Long id) {
        return edgeService.findById(id).orElse(null);
    }

    @PostMapping
    public Edge create(@RequestBody Edge edge) {
        return edgeService.save(edge);
    }

    @PutMapping("/{id}")
    public Edge update(@PathVariable Long id, @RequestBody Edge edge) {
        edge.setId(id);
        return edgeService.save(edge);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        edgeService.delete(id);
    }
}

