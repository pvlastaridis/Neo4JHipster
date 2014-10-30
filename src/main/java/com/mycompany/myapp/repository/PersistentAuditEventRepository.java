package com.mycompany.myapp.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.mycompany.myapp.domain.PersistentAuditEvent;



public interface PersistentAuditEventRepository extends GraphRepository<PersistentAuditEvent>{

	 List<PersistentAuditEvent> findByPrincipal(String principal);

	 List<PersistentAuditEvent> findByPrincipalAndAuditEventDateGreaterThan(String principal, Long after);
	 
	 @Query("MATCH (`persistentAuditEvent`:`PersistentAuditEvent`) WHERE " +
	 "`persistentAuditEvent`.`auditEventDate` >= {0} AND " +
	 "`persistentAuditEvent`.`auditEventDate` <= {1}" +
	 "RETURN `persistentAuditEvent`")
	 List<PersistentAuditEvent> findByAuditEventDateBetween(Long fromDate, Long toDate);
}
