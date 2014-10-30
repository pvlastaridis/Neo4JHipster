package com.mycompany.myapp.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.mycompany.myapp.domain.PersistentAuditEvent;



public interface PersistentAuditEventRepository extends GraphRepository<PersistentAuditEvent>{

	 List<PersistentAuditEvent> findByPrincipal(String principal);

	 List<PersistentAuditEvent> findByPrincipalAndAuditEventDateGreaterThan(String principal, Long after);
}
