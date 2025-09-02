package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.RoleDto;
import com.wikigroup.desarrolloweb.model.Role;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.service.RoleService;
import com.wikigroup.desarrolloweb.service.EmpresaService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService service;
    private final EmpresaService empresaService;
    private final ModelMapper mapper;

    public RoleController(RoleService service, EmpresaService empresaService, ModelMapper mapper) {
        this.service = service;
        this.empresaService = empresaService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<RoleDto> getAll() {
        return service.findAll()
                .stream()
                .map(r -> mapper.map(r, RoleDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RoleDto getById(@PathVariable Long id) {
        Role role = service.findById(id);
        return mapper.map(role, RoleDto.class);
    }

    @PostMapping
    public RoleDto create(@RequestBody RoleDto dto) {
        Empresa empresa = empresaService.findById(dto.getEmpresaId());
        Role role = mapper.map(dto, Role.class);
        role.setEmpresa(empresa);
        return mapper.map(service.save(role), RoleDto.class);
    }

    @PutMapping("/{id}")
    public RoleDto update(@PathVariable Long id, @RequestBody RoleDto dto) {
        Empresa empresa = empresaService.findById(dto.getEmpresaId());
        Role role = mapper.map(dto, Role.class);
        role.setId(id);
        role.setEmpresa(empresa);
        return mapper.map(service.save(role), RoleDto.class);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
