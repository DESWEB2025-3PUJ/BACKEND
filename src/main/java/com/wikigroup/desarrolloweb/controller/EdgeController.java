package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.EdgeDto;
import com.wikigroup.desarrolloweb.service.EdgeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/edges")
public class EdgeController {

    private final EdgeService service;

    public EdgeController(EdgeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EdgeDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EdgeDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-process/{processId}")
    public ResponseEntity<List<EdgeDto>> getByProcess(@PathVariable Long processId) {
    return ResponseEntity.ok(service.getByProcessId(processId));
    }


    @PostMapping
    public ResponseEntity<EdgeDto> create(@Valid @RequestBody EdgeDto dto) {
        EdgeDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/edges/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EdgeDto> update(@PathVariable Long id, @Valid @RequestBody EdgeDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}