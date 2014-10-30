package com.mycompany.myapp.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.mycompany.myapp.domain.PersistentAuditEventData;



public interface PersistentAuditEventDataRepository extends GraphRepository<PersistentAuditEventData> {

}
