package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.GatewayDto;
import com.wikigroup.desarrolloweb.service.GatewayService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/gateways")
public class GatewayController {

    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<GatewayDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GatewayDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-process/{processId}")
    public ResponseEntity<List<GatewayDto>> getByProcess(@PathVariable Long processId) {
    return ResponseEntity.ok(service.getByProcessId(processId));
    }


    @PostMapping
    public ResponseEntity<GatewayDto> create(@Valid @RequestBody GatewayDto dto) {
        GatewayDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/gateways/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GatewayDto> update(@PathVariable Long id, @Valid @RequestBody GatewayDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

