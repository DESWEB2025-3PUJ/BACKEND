package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessRepository extends JpaRepository<Process, Long> {}
public interface ActivityRepository extends JpaRepository<Activity, Long> {}
public interface EdgeRepository extends JpaRepository<Edge, Long> {}
