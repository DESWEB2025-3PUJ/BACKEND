package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.ProcessDto;
import com.wikigroup.desarrolloweb.service.ProcessService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/processes")
public class ProcessController {

    private final ProcessService service;

    public ProcessController(ProcessService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProcessDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-empresa/{empresaId}")
    public ResponseEntity<List<ProcessDto>> getByEmpresa(@PathVariable Long empresaId) {
    return ResponseEntity.ok(service.getByEmpresaId(empresaId));
    }

    @PostMapping
    public ResponseEntity<ProcessDto> create(@Valid @RequestBody ProcessDto dto) {
        ProcessDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/processes/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcessDto> update(@PathVariable Long id, @Valid @RequestBody ProcessDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


