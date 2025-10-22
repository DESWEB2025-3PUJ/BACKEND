package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.model.Activity;
import com.wikigroup.desarrolloweb.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository repository;

    public ActivityService(ActivityRepository repository) {
        this.repository = repository;
    }

    public List<Activity> findAll() {
        return repository.findAll();
    }

    public Activity findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with id " + id));
    }

    public Activity save(Activity activity) {
        return repository.save(activity);
    }

    public List<Activity> findByProcessId(Long processId) {
        return repository.findByProcessId(processId);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Activity not found with id " + id);
        }
        repository.deleteById(id);
    }
}

