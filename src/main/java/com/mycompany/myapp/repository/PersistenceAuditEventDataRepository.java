package com.mycompany.myapp.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.mycompany.myapp.domain.PersistentAuditEventData;


/**
 * Spring Data Neo4JDB repository for the PersistentAuditEventData entity.
 */
public interface PersistenceAuditEventDataRepository extends GraphRepository<PersistentAuditEventData> {

}
