package com.wikigroup.desarrolloweb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wikigroup.desarrolloweb.dtos.EdgeDto;
import com.wikigroup.desarrolloweb.model.Activity;
import com.wikigroup.desarrolloweb.model.BpmProcess;
import com.wikigroup.desarrolloweb.model.Edge;
import com.wikigroup.desarrolloweb.repository.EdgeRepository;
import com.wikigroup.desarrolloweb.shared.NotFoundException;

@Service
@Transactional
public class EdgeService {

    private final EdgeRepository repository;
    private final ActivityService activityService;
    private final ProcessService processService;
    private final ModelMapper mapper;

    public EdgeService(EdgeRepository repository, ActivityService activityService, ProcessService processService, ModelMapper mapper) {
        this.repository = repository;
        this.activityService = activityService;
        this.processService = processService;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<EdgeDto> getAll() {
        return repository.findAll().stream()
                .map(e -> mapper.map(e, EdgeDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EdgeDto getById(Long id) {
        Edge edge = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Edge not found with id " + id));
        return mapper.map(edge, EdgeDto.class);
    }

    public EdgeDto create(EdgeDto dto) {
        Edge entity = toEntityWithRelations(dto, null);
        Edge saved = repository.save(entity);
        return mapper.map(saved, EdgeDto.class);
    }

    public EdgeDto update(Long id, EdgeDto dto) {
        // Garantiza existencia
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Edge not found with id " + id));

        Edge entity = toEntityWithRelations(dto, id);
        Edge saved = repository.save(entity);
        return mapper.map(saved, EdgeDto.class);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Edge not found with id " + id);
        }
        repository.deleteById(id);
    }

    // -------- helpers --------
    private Edge toEntityWithRelations(EdgeDto dto, Long idOrNull) {
        Edge entity = mapper.map(dto, Edge.class);
        if (idOrNull != null) entity.setId(idOrNull);

        // Resolver relaciones por id
        Activity source = activityService.findEntityById(dto.getActivitySourceId());
        Activity destiny = activityService.findEntityById(dto.getActivityDestinyId());
        BpmProcess process = processService.findEntityById(dto.getProcessId());

        entity.setActivitySource(source);
        entity.setActivityDestiny(destiny);
        entity.setProcess(process);

        return entity;
    }
}



