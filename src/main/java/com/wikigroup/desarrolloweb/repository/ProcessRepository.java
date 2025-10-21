package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.BpmProcess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessRepository extends JpaRepository<BpmProcess, Long> {}
