package com.wikigroup.desarrolloweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wikigroup.desarrolloweb.model.Activity;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByProcessId(Long processId);
}
