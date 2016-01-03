package com.mycompany.myapp.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

/**
 * Persist AuditEvent managed by the Spring Boot actuator
 * @see org.springframework.boot.actuate.audit.AuditEvent
 */
@NodeEntity
public class PersistentAuditEvent extends Entity {

    @NotNull
    private String principal;

    private Long auditEventDate;

    private String auditEventType;

    @Relationship
    private Set<PersistentAuditEventData> data = new HashSet<>();

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Long getAuditEventDate() { return auditEventDate; }

    public void setAuditEventDate(Long auditEventDate) { this.auditEventDate = auditEventDate; }

    public LocalDateTime getAuditEventDDate() {
        Instant instant = Instant.ofEpochMilli(auditEventDate);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());
        return ldt; }

    public void setAuditEventDDate(LocalDateTime auditEventDate) { this.auditEventDate =
        auditEventDate.toInstant(ZoneOffset.ofHours(2)).toEpochMilli(); }

    public String getAuditEventType() {
        return auditEventType;
    }

    public void setAuditEventType(String auditEventType) {
        this.auditEventType = auditEventType;
    }

    public Set<PersistentAuditEventData> getData() { return data; }

    public void setData(Set<PersistentAuditEventData> data) { this.data = data; }
}
