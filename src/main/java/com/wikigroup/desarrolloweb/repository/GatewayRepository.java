package com.wikigroup.desarrolloweb.repository;

import com.wikigroup.desarrolloweb.model.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GatewayRepository extends JpaRepository<Gateway, Long> {
    List<Gateway> findByProcessId(Long processId);
}
