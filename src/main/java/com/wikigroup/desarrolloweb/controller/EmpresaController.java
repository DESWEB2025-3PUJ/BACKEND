package com.wikigroup.desarrolloweb.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wikigroup.desarrolloweb.dtos.EmpresaDto;
import com.wikigroup.desarrolloweb.service.EmpresaService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controlador de Empresas con seguridad JWT y control de roles.
 * 
 * Requiere autenticación JWT válida para todos los endpoints.
 * Algunos endpoints están restringidos por rol usando @Secured.
 */
@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    /**
     * Obtener todas las empresas
     * Disponible para: ADMINISTRADOR, EDITOR, SOLO_LECTURA
     */
    @GetMapping
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_EDITOR", "ROLE_SOLO_LECTURA"})
    public List<EmpresaDto> getAll() {
        return service.findAll()
                .stream()
                .map(e -> mapper.map(e, EmpresaDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Obtener empresa por ID
     * Disponible para: ADMINISTRADOR, EDITOR, SOLO_LECTURA
     */
    @GetMapping("/{id}")
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_EDITOR", "ROLE_SOLO_LECTURA"})
    public EmpresaDto getById(@PathVariable Long id) {
        Empresa empresa = service.findById(id);
        return mapper.map(empresa, EmpresaDto.class);
    }

    /**
     * Crear nueva empresa
     * Disponible solo para: ADMINISTRADOR
     */
    @PostMapping
    @Secured("ROLE_ADMINISTRADOR")
    public EmpresaDto create(@RequestBody EmpresaDto dto) {
        Empresa empresa = mapper.map(dto, Empresa.class);
        return mapper.map(service.save(empresa), EmpresaDto.class);
    }

    /**
     * Actualizar empresa existente
     * Disponible para: ADMINISTRADOR, EDITOR
     */
    @PutMapping("/{id}")
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_EDITOR"})
    public EmpresaDto update(@PathVariable Long id, @RequestBody EmpresaDto dto) {
        Empresa empresa = mapper.map(dto, Empresa.class);
        empresa.setId(id);
        return mapper.map(service.save(empresa), EmpresaDto.class);
    }

    /**
     * Eliminar empresa
     * Disponible solo para: ADMINISTRADOR
     */
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMINISTRADOR")
    public void delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

