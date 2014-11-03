package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PersistentAuditEvent;

import org.joda.time.LocalDateTime;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

/**
 * Spring Data Neo4JDB repository for the PersistentAuditEvent entity.
 */
public interface PersistenceAuditEventRepository extends
		GraphRepository<PersistentAuditEvent> {

	List<PersistentAuditEvent> findByPrincipal(String principal);

	List<PersistentAuditEvent> findByPrincipalAndAuditEventDateGreaterThan(
			String principal, Long after);

	@Query("MATCH (`persistentAuditEvent`:`PersistentAuditEvent`) WHERE "
			+ "`persistentAuditEvent`.`auditEventDate` >= {0} AND "
			+ "`persistentAuditEvent`.`auditEventDate` <= {1}"
			+ "RETURN `persistentAuditEvent`")
	List<PersistentAuditEvent> findByAuditEventDateBetween(Long fromDate,
			Long toDate);
}