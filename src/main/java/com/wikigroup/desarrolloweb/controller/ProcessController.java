package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.model.Process;
import com.wikigroup.desarrolloweb.service.ProcessService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processes")
public class ProcessController {

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping
    public List<Process> getAll() {
        return processService.findAll();
    }

    @GetMapping("/{id}")
    public Process getById(@PathVariable Long id) {
        return processService.findById(id).orElse(null);
    }

    @PostMapping
    public Process create(@RequestBody Process process) {
        return processService.save(process);
    }

    @PutMapping("/{id}")
    public Process update(@PathVariable Long id, @RequestBody Process process) {
        process.setId(id);
        return processService.save(process);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        processService.delete(id);
    }
}
