package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.dtos.GatewayDto;
import com.wikigroup.desarrolloweb.model.Gateway;
import com.wikigroup.desarrolloweb.model.Process;
import com.wikigroup.desarrolloweb.service.GatewayService;
import com.wikigroup.desarrolloweb.service.ProcessService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gateways")
public class GatewayController {

    private final GatewayService service;
    private final ProcessService processService;
    private final ModelMapper mapper;

    public GatewayController(GatewayService service, ProcessService processService, ModelMapper mapper) {
        this.service = service;
        this.processService = processService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<GatewayDto> getAll() {
        return service.findAll()
                .stream()
                .map(g -> mapper.map(g, GatewayDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GatewayDto getById(@PathVariable Long id) {
        Gateway gateway = service.findById(id);
        return mapper.map(gateway, GatewayDto.class);
    }

    @GetMapping("/process/{processId}")
    public List<GatewayDto> getByProcess(@PathVariable Long processId) {
        return service.findByProcessId(processId)
                .stream()
                .map(g -> mapper.map(g, GatewayDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public GatewayDto create(@RequestBody GatewayDto dto) {
        Process process = processService.findById(dto.getProcessId());
        Gateway gateway = mapper.map(dto, Gateway.class);
        gateway.setProcess(process);
        return mapper.map(service.save(gateway), GatewayDto.class);
    }

    @PutMapping("/{id}")
    public GatewayDto update(@PathVariable Long id, @RequestBody GatewayDto dto) {
        Process process = processService.findById(dto.getProcessId());
        Gateway gateway = mapper.map(dto, Gateway.class);
        gateway.setId(id);
        gateway.setProcess(process);
        return mapper.map(service.save(gateway), GatewayDto.class);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

