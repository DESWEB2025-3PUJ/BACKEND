package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.ActivityDto;
import com.wikigroup.desarrolloweb.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService service;

    public ActivityController(ActivityService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ActivityDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    //listar actividades por processId
    @GetMapping("/by-process/{processId}")
    public ResponseEntity<List<ActivityDto>> getByProcess(@PathVariable Long processId) {
        return ResponseEntity.ok(service.getByProcessId(processId));
    }

    @PostMapping
    public ResponseEntity<ActivityDto> create(@Valid @RequestBody ActivityDto dto) {
        ActivityDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/activities/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDto> update(@PathVariable Long id, @Valid @RequestBody ActivityDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

