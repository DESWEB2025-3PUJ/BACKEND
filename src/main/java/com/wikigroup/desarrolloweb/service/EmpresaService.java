package com.wikigroup.desarrolloweb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wikigroup.desarrolloweb.dtos.EmpresaDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import com.wikigroup.desarrolloweb.shared.NotFoundException;

@Service
@Transactional
public class EmpresaService {

    private final EmpresaRepository repository;
    private final ModelMapper mapper;

    public EmpresaService(EmpresaRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<EmpresaDto> getAll() {
        return repository.findAll().stream()
                .map(e -> mapper.map(e, EmpresaDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmpresaDto getById(Long id) {
        Empresa empresa = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empresa not found with id " + id));
        return mapper.map(empresa, EmpresaDto.class);
    }

    // Opcional: útil para otros services que necesiten la ENTIDAD
    @Transactional(readOnly = true)
    public Empresa findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empresa not found with id " + id));
    }

    public EmpresaDto create(EmpresaDto dto) {
        Empresa entity = mapper.map(dto, Empresa.class);
        // Si en el futuro necesitas setear relaciones explícitas, hazlo aquí antes del save
        Empresa saved = repository.save(entity);
        return mapper.map(saved, EmpresaDto.class);
    }

    public EmpresaDto update(Long id, EmpresaDto dto) {
        // Garantiza existencia
        Empresa current = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empresa not found with id " + id));

        // Actualiza campos (dos opciones: map directo o seteo selectivo)
        // Opción simple: map a una nueva entidad y asegura el id
        Empresa entity = mapper.map(dto, Empresa.class);
        entity.setId(current.getId());

        Empresa saved = repository.save(entity);
        return mapper.map(saved, EmpresaDto.class);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Empresa not found with id " + id);
        }
        repository.deleteById(id);
    }
}


