package com.wikigroup.desarrolloweb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wikigroup.desarrolloweb.dtos.ActivityDto;
import com.wikigroup.desarrolloweb.model.Activity;
import com.wikigroup.desarrolloweb.model.BpmProcess;
import com.wikigroup.desarrolloweb.model.Role;
import com.wikigroup.desarrolloweb.repository.ActivityRepository;
import com.wikigroup.desarrolloweb.shared.NotFoundException;

@Service
@Transactional
public class ActivityService {

    private final ActivityRepository repository;
    private final ProcessService processService;
    private final RoleService roleService;
    private final ModelMapper mapper;

    public ActivityService(ActivityRepository repository, ProcessService processService, RoleService roleService, ModelMapper mapper) {
        this.repository = repository;
        this.processService = processService;
        this.roleService = roleService;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ActivityDto> getAll() {
        return repository.findAll().stream()
                .map(a -> mapper.map(a, ActivityDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ActivityDto getById(Long id) {
        Activity activity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Activity not found with id " + id));
        return mapper.map(activity, ActivityDto.class);
    }

    // ENTIDAD para uso interno entre servicios
    @Transactional(readOnly = true)
    public Activity findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Activity not found with id " + id));
    }

    public ActivityDto create(ActivityDto dto) {
        Activity entity = toEntityWithRelations(dto, null);
        Activity saved = repository.save(entity);
        return mapper.map(saved, ActivityDto.class);
    }

    public ActivityDto update(Long id, ActivityDto dto) {
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Activity not found with id " + id));
        Activity entity = toEntityWithRelations(dto, id);
        Activity saved = repository.save(entity);
        return mapper.map(saved, ActivityDto.class);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Activity not found with id " + id);
        }
        repository.deleteById(id);
    }

    // -------- helpers --------
    private Activity toEntityWithRelations(ActivityDto dto, Long idOrNull) {
        Activity entity = mapper.map(dto, Activity.class);
        if (idOrNull != null) entity.setId(idOrNull);

        if (dto.getProcessId() != null) {
            BpmProcess process = processService.findEntityById(dto.getProcessId());
            entity.setProcess(process);
        } else {
            entity.setProcess(null);
        }

        if (dto.getRoleId() != null) {
            Role role = roleService.findEntityById(dto.getRoleId());
            entity.setRole(role);
        } else {
            entity.setRole(null);
        }

        return entity;
    }
}


