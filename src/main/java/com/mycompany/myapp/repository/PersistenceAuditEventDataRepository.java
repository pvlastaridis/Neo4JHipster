package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PersistentAuditEventData;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4JDB repository for the PersistentAuditEventData entity.
 */
@Repository
public interface PersistenceAuditEventDataRepository extends GraphRepository<PersistentAuditEventData> {
}
