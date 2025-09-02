package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.EmpresaDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.service.EmpresaService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService service;
    private final ModelMapper mapper;

    public EmpresaController(EmpresaService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<EmpresaDto> getAll() {
        return service.findAll()
                .stream()
                .map(e -> mapper.map(e, EmpresaDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EmpresaDto getById(@PathVariable Long id) {
        Empresa empresa = service.findById(id);
        return mapper.map(empresa, EmpresaDto.class);
    }

    @PostMapping
    public EmpresaDto create(@RequestBody EmpresaDto dto) {
        Empresa empresa = mapper.map(dto, Empresa.class);
        return mapper.map(service.save(empresa), EmpresaDto.class);
    }

    @PutMapping("/{id}")
    public EmpresaDto update(@PathVariable Long id, @RequestBody EmpresaDto dto) {
        Empresa empresa = mapper.map(dto, Empresa.class);
        empresa.setId(id);
        return mapper.map(service.save(empresa), EmpresaDto.class);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
