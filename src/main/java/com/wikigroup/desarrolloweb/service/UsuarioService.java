package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.repository.UsuarioRepository;
import com.wikigroup.desarrolloweb.shared.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository repository;
    private final EmpresaService empresaService; // resolver FK empresa
    private final ModelMapper mapper;

    public UsuarioService(UsuarioRepository repository, EmpresaService empresaService, ModelMapper mapper) {
        this.repository = repository;
        this.empresaService = empresaService;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> getAll() {
        return repository.findAll().stream()
                .map(u -> mapper.map(u, UsuarioDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDto getById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario not found with id " + id));
        return mapper.map(usuario, UsuarioDto.class);
    }

    // Opcional: útil para otros services que necesiten la ENTIDAD
    @Transactional(readOnly = true)
    public Usuario findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario not found with id " + id));
    }

    public UsuarioDto create(UsuarioDto dto) {
        Usuario entity = toEntityWithRelations(dto, null);
        Usuario saved = repository.save(entity);
        return mapper.map(saved, UsuarioDto.class);
    }

    public UsuarioDto update(Long id, UsuarioDto dto) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario not found with id " + id));
        Usuario entity = toEntityWithRelations(dto, id);
        Usuario saved = repository.save(entity);
        return mapper.map(saved, UsuarioDto.class);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Usuario not found with id " + id);
        }
        repository.deleteById(id);
    }

    // -------- helpers --------
    private Usuario toEntityWithRelations(UsuarioDto dto, Long idOrNull) {
        Usuario entity = mapper.map(dto, Usuario.class);
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

