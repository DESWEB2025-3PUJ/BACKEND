package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.dtos.ProcessDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.model.BpmProcess;
import com.wikigroup.desarrolloweb.model.Role;
import com.wikigroup.desarrolloweb.repository.ProcessRepository;
import com.wikigroup.desarrolloweb.shared.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProcessService {

    private final ProcessRepository repository;     // JpaRepository<BpmProcess, Long>
    private final EmpresaService empresaService;    // resolver FK empresa (entidad)
    private final RoleService roleService;          // resolver FK role (entidad)
    private final ModelMapper mapper;

    public ProcessService(ProcessRepository repository, EmpresaService empresaService, RoleService roleService, ModelMapper mapper) {
        this.repository = repository;
        this.empresaService = empresaService;
        this.roleService = roleService;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ProcessDto> getAll() {
        return repository.findAll().stream()
                .map(p -> mapper.map(p, ProcessDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProcessDto getById(Long id) {
        BpmProcess process = repository.findById(id).orElseThrow(() -> new NotFoundException("Process not found with id " + id));
        return mapper.map(process, ProcessDto.class);
    }

    // ENTIDAD para otros servicios
    @Transactional(readOnly = true)
    public BpmProcess findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Process not found with id " + id));
    }

    public ProcessDto create(ProcessDto dto) {
        BpmProcess entity = toEntityWithRelations(dto, null);
        BpmProcess saved = repository.save(entity);
        return mapper.map(saved, ProcessDto.class);
    }

    public ProcessDto update(Long id, ProcessDto dto) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Process not found with id " + id));

        BpmProcess entity = toEntityWithRelations(dto, id);
        BpmProcess saved = repository.save(entity);
        return mapper.map(saved, ProcessDto.class);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Process not found with id " + id);
        }
        repository.deleteById(id);
    }

    // -------- helpers --------
    private BpmProcess toEntityWithRelations(ProcessDto dto, Long idOrNull) {
        BpmProcess entity = mapper.map(dto, BpmProcess.class);
        if (idOrNull != null) entity.setId(idOrNull);

        // empresa (FK)
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaService.findEntityById(dto.getEmpresaId());
            entity.setEmpresa(empresa);
        } else {
            entity.setEmpresa(null);
        }

        // role (FK) — opcional
        if (dto.getRoleId() != null) {
            Role role = roleService.findEntityById(dto.getRoleId());
            entity.setRole(role);
        } else {
            entity.setRole(null);
        }

        return entity;
    }
}



