package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.ProcessDto;
import com.wikigroup.desarrolloweb.model.Process;
import com.wikigroup.desarrolloweb.service.ProcessService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/processes")
public class ProcessController {

    private final ProcessService service;
    private final RoleService roleService; 
    private final ModelMapper mapper;

    public ProcessController(ProcessService service, RoleService roleService, ModelMapper mapper) {
        this.service = service;
        this.roleService = roleService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ProcessDto> getAll() {
        return service.findAll()
                .stream()
                .map(p -> mapper.map(p, ProcessDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProcessDto getById(@PathVariable Long id) {
        return mapper.map(service.findById(id), ProcessDto.class);
    }

    @PostMapping
    public ProcessDto create(@RequestBody ProcessDto dto) {
        Process process = mapper.map(dto, Process.class);

        // ✅ usar roleId del DTO
        if (dto.getRoleId() != null) {
            process.setRole(roleService.findById(dto.getRoleId()));
        }

        return mapper.map(service.save(process), ProcessDto.class);
    }

    @PutMapping("/{id}")
    public ProcessDto update(@PathVariable Long id, @RequestBody ProcessDto dto) {
        Process process = mapper.map(dto, Process.class);
        process.setId(id);

        if (dto.getRoleId() != null) {
            process.setRole(roleService.findById(dto.getRoleId()));
        }

        return mapper.map(service.save(process), ProcessDto.class);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

