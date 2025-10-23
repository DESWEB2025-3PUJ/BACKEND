package com.wikigroup.desarrolloweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wikigroup.desarrolloweb.model.Edge;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
    List<Edge> findByProcessId(Long processId);
}

