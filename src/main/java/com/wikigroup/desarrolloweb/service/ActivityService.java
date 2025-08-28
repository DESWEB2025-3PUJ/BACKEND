package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.model.Activity;
import com.wikigroup.desarrolloweb.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> getAll() {
        return activityRepository.findAll();
    }

    public Activity getById(Long id) {
        return activityRepository.findById(id).orElse(null);
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    public void delete(Long id) {
        activityRepository.deleteById(id);
    }
}
