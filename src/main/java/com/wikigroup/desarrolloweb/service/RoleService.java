package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.dtos.RoleDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.model.Role;
import com.wikigroup.desarrolloweb.repository.RoleRepository;
import com.wikigroup.desarrolloweb.shared.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private final RoleRepository repository;
    private final EmpresaService empresaService;   // para resolver FK empresa
    private final ModelMapper mapper;

    public RoleService(RoleRepository repository, EmpresaService empresaService, ModelMapper mapper) {
        this.repository = repository;
        this.empresaService = empresaService;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<RoleDto> getAll() {
        return repository.findAll().stream()
                .map(r -> mapper.map(r, RoleDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoleDto getById(Long id) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found with id " + id));
        return mapper.map(role, RoleDto.class);
    }

    // listar por empresa en formato DTO
    @Transactional(readOnly = true)
    public List<RoleDto> getByEmpresaId(Long empresaId) {
        // Validación temprana: asegura que la empresa exista
        empresaService.findEntityById(empresaId);

        return repository.findByEmpresaId(empresaId).stream()
                .map(r -> mapper.map(r, RoleDto.class))
                .collect(Collectors.toList());
    }

    // Útil para otros services que necesiten la ENTIDAD Role
    @Transactional(readOnly = true)
    public Role findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found with id " + id));
    }

    public RoleDto create(RoleDto dto) {
        Role entity = toEntityWithRelations(dto, null);
        Role saved = repository.save(entity);
        return mapper.map(saved, RoleDto.class);
    }

    public RoleDto update(Long id, RoleDto dto) {
        // garantizar existencia
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found with id " + id));

        Role entity = toEntityWithRelations(dto, id);
        Role saved = repository.save(entity);
        return mapper.map(saved, RoleDto.class);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Role not found with id " + id);
        }
        repository.deleteById(id);
    }

    // ------- helpers -------
    private Role toEntityWithRelations(RoleDto dto, Long idOrNull) {
        Role entity = mapper.map(dto, Role.class);
        if (idOrNull != null) entity.setId(idOrNull);

        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaService.findEntityById(dto.getEmpresaId()); // ENTIDAD
            entity.setEmpresa(empresa);
        } else {
            entity.setEmpresa(null);
        }

        return entity;
    }
}
