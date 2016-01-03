package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PersistentAuditEvent;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data Neo4jDB repository for the PersistentAuditEvent entity.
 */
public interface PersistenceAuditEventRepository extends GraphRepository<PersistentAuditEvent> {

    List<PersistentAuditEvent> findByPrincipal(String principal);

    PersistentAuditEvent findOneById(String id);

    @Query("MATCH (ps:PersistentAuditEvent {principal: {0} }) WHERE "
        + "ps.auditEventDate >= {1} "
        + "RETURN ps")
    List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(
        String principal, Long after);

    @Query("MATCH (ps:PersistentAuditEvent) WHERE ps.auditEventDate >= {0} AND "
        + "ps.auditEventDate <= {1} RETURN ps")
    List<PersistentAuditEvent> findAllByAuditEventDateBetween(Long fromDate, Long toDate);
}
