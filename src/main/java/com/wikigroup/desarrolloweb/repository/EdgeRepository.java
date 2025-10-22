package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.Edge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
    List<Edge> findByProcessId(Long processId);
}

