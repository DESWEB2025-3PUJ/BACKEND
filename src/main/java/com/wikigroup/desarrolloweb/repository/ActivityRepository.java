package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByProcessId(Long processId);
}
