package com.wikigroup.desarrolloweb.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wikigroup.desarrolloweb.model.Gateway;

import java.util.List;

public interface GatewayRepository extends JpaRepository<Gateway, Long> {
    List<Gateway> findByProcessId(Long processId);
}
