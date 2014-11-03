package com.mycompany.myapp.domain;


import org.joda.time.LocalDateTime;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Persist AuditEvent managed by the Spring Boot actuator
 * @see org.springframework.boot.actuate.audit.AuditEvent
 */


@NodeEntity
public class PersistentAuditEvent  {

	@GraphId
	Long id;

    @NotNull
    private String principal;

    
    private Long auditEventDate;
    
    private String auditEventType;

    @RelatedTo(direction = Direction.OUTGOING)
   	@Fetch
   	private Set<PersistentAuditEventData> data = new HashSet<PersistentAuditEventData>();

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public LocalDateTime getAuditEventDate() {
        return new LocalDateTime(auditEventDate);
    }

    public void setAuditEventDate(LocalDateTime auditEventDate) {
        this.auditEventDate = auditEventDate.toDateTime().getMillis();
    }

    public String getAuditEventType() {
        return auditEventType;
    }

    public void setAuditEventType(String auditEventType) {
        this.auditEventType = auditEventType;
    }

    public Map<String, String> getData() {
		Map<String, String> mdata = new HashMap<>();
		for (PersistentAuditEventData paed : data) {
			mdata.put(paed.getName(), paed.getValue());
		}		
		return mdata;
	}
	
	public void setData(Set<PersistentAuditEventData> data) {
		this.data = data;
	}
}
