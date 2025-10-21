package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.dtos.GatewayDto;
import com.wikigroup.desarrolloweb.model.Gateway;
import com.wikigroup.desarrolloweb.model.BpmProcess;
import com.wikigroup.desarrolloweb.repository.GatewayRepository;
import com.wikigroup.desarrolloweb.shared.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GatewayService {

    private final GatewayRepository repository;
    private final ProcessService processService;  // para resolver la relación
    private final ModelMapper mapper;

    public GatewayService(GatewayRepository repository,ProcessService processService, ModelMapper mapper) {
        this.repository = repository;
        this.processService = processService;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<GatewayDto> getAll() {
        return repository.findAll().stream()
                .map(g -> mapper.map(g, GatewayDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GatewayDto getById(Long id) {
        Gateway gateway = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gateway not found with id " + id));
        return mapper.map(gateway, GatewayDto.class);
    }

    // Opcional: útil si algún otro service necesita la ENTIDAD
    @Transactional(readOnly = true)
    public Gateway findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gateway not found with id " + id));
    }

    public GatewayDto create(GatewayDto dto) {
        Gateway entity = toEntityWithRelations(dto, null);
        Gateway saved = repository.save(entity);
        return mapper.map(saved, GatewayDto.class);
    }

    public GatewayDto update(Long id, GatewayDto dto) {
        // garantiza existencia
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gateway not found with id " + id));

        Gateway entity = toEntityWithRelations(dto, id);
        Gateway saved = repository.save(entity);
        return mapper.map(saved, GatewayDto.class);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Gateway not found with id " + id);
        }
        repository.deleteById(id);
    }

    // -------- helpers --------
    private Gateway toEntityWithRelations(GatewayDto dto, Long idOrNull) {
        Gateway entity = mapper.map(dto, Gateway.class);
        if (idOrNull != null) entity.setId(idOrNull);

        // relacion obligatoria Process
        BpmProcess process = processService.findEntityById(dto.getProcessId()); // asume que devuelve ENTIDAD
        entity.setProcess(process);

        return entity;
    }
}
