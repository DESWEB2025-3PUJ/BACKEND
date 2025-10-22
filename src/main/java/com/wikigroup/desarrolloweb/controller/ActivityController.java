package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.ActivityDto;
import com.wikigroup.desarrolloweb.model.Activity;
import com.wikigroup.desarrolloweb.model.Process;
import com.wikigroup.desarrolloweb.service.ActivityService;
import com.wikigroup.desarrolloweb.service.ProcessService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService service;
    private final ProcessService processService;
    private final ModelMapper mapper;

    public ActivityController(ActivityService service, ProcessService processService, ModelMapper mapper) {
        this.service = service;
        this.processService = processService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ActivityDto> getAll() {
        return service.findAll()
                .stream()
                .map(a -> mapper.map(a, ActivityDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ActivityDto getById(@PathVariable Long id) {
        Activity activity = service.findById(id);
        return mapper.map(activity, ActivityDto.class);
    }

    @PostMapping
    public ActivityDto create(@RequestBody ActivityDto dto) {
        Process process = processService.findById(dto.getProcessId());
        Activity activity = mapper.map(dto, Activity.class);
        activity.setProcess(process);
        return mapper.map(service.save(activity), ActivityDto.class);
    }

    @PutMapping("/{id}")
    public ActivityDto update(@PathVariable Long id, @RequestBody ActivityDto dto) {
        Process process = processService.findById(dto.getProcessId());
        Activity activity = mapper.map(dto, Activity.class);
        activity.setId(id);
        activity.setProcess(process);
        return mapper.map(service.save(activity), ActivityDto.class);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}


