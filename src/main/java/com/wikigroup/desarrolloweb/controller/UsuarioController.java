package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.service.UsuarioService;
import com.wikigroup.desarrolloweb.service.EmpresaService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final EmpresaService empresaService;
    private final ModelMapper mapper;

    public UsuarioController(UsuarioService service, EmpresaService empresaService, ModelMapper mapper) {
        this.service = service;
        this.empresaService = empresaService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<UsuarioDto> getAll() {
        return service.findAll()
                .stream()
                .map(u -> mapper.map(u, UsuarioDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UsuarioDto getById(@PathVariable Long id) {
        Usuario usuario = service.findById(id);
        return mapper.map(usuario, UsuarioDto.class);
    }

    @PostMapping
    public UsuarioDto create(@RequestBody UsuarioDto dto) {
        Empresa empresa = empresaService.findById(dto.getEmpresaId());
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setEmpresa(empresa);
        return mapper.map(service.save(usuario), UsuarioDto.class);
    }

    @PutMapping("/{id}")
    public UsuarioDto update(@PathVariable Long id, @RequestBody UsuarioDto dto) {
        Empresa empresa = empresaService.findById(dto.getEmpresaId());
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setId(id);
        usuario.setEmpresa(empresa);
        return mapper.map(service.save(usuario), UsuarioDto.class);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
