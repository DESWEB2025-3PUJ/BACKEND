package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.EdgeDto;
import com.wikigroup.desarrolloweb.model.Activity;
import com.wikigroup.desarrolloweb.model.Edge;
import com.wikigroup.desarrolloweb.model.Process;
import com.wikigroup.desarrolloweb.service.ActivityService;
import com.wikigroup.desarrolloweb.service.EdgeService;
import com.wikigroup.desarrolloweb.service.ProcessService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/edges")
public class EdgeController {

    private final EdgeService service;
    private final ActivityService activityService;
    private final ProcessService processService;
    private final ModelMapper mapper;

    public EdgeController(EdgeService service, ActivityService activityService, ProcessService processService, ModelMapper mapper) {
        this.service = service;
        this.activityService = activityService;
        this.processService = processService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<EdgeDto> getAll() {
        return service.findAll()
                .stream()
                .map(e -> mapper.map(e, EdgeDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EdgeDto getById(@PathVariable Long id) {
        Edge edge = service.findById(id);
        return mapper.map(edge, EdgeDto.class);
    }

    @PostMapping
    public EdgeDto create(@RequestBody EdgeDto dto) {
        Activity source = activityService.findById(dto.getActivitySourceId());
        Activity destiny = activityService.findById(dto.getActivityDestinyId());
        Process process = processService.findById(dto.getProcessId());

        Edge edge = mapper.map(dto, Edge.class);
        edge.setActivitySource(source);
        edge.setActivityDestiny(destiny);
        edge.setProcess(process);
        
        return mapper.map(service.save(edge), EdgeDto.class);
    }

    @PutMapping("/{id}")
    public EdgeDto update(@PathVariable Long id, @RequestBody EdgeDto dto) {
        Activity source = activityService.findById(dto.getActivitySourceId());
        Activity destiny = activityService.findById(dto.getActivityDestinyId());
        Process process = processService.findById(dto.getProcessId());

        Edge edge = mapper.map(dto, Edge.class);
        edge.setId(id);
        edge.setActivitySource(source);
        edge.setActivityDestiny(destiny);
        edge.setProcess(process);

        return mapper.map(service.save(edge), EdgeDto.class);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

