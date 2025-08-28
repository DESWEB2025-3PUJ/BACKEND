package com.wikigroup.desarrolloweb.controller;

import com.wikigroup.desarrolloweb.model.Activity;
import com.wikigroup.desarrolloweb.service.ActivityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public List<Activity> getAll() {
        return activityService.findAll();
    }

    @GetMapping("/{id}")
    public Activity getById(@PathVariable Long id) {
        return activityService.findById(id).orElse(null);
    }

    @PostMapping
    public Activity create(@RequestBody Activity activity) {
        return activityService.save(activity);
    }

    @PutMapping("/{id}")
    public Activity update(@PathVariable Long id, @RequestBody Activity activity) {
        activity.setId(id);
        return activityService.save(activity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        activityService.delete(id);
    }
}

