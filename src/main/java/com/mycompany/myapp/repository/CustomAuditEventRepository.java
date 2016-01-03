package com.mycompany.myapp.repository;

import com.mycompany.myapp.config.audit.AuditEventConverter;
import com.mycompany.myapp.domain.PersistentAuditEvent;

import com.mycompany.myapp.domain.PersistentAuditEventData;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Wraps an implementation of Spring Boot's AuditEventRepository.
 */
@Repository
public class CustomAuditEventRepository {

    @Inject
    private PersistenceAuditEventRepository persistenceAuditEventRepository;

    @Inject
    private PersistenceAuditEventDataRepository persistenceAuditEventDataRepository;

    @Bean
    public AuditEventRepository auditEventRepository() {
        return new AuditEventRepository() {

            private static final String AUTHORIZATION_FAILURE = "AUTHORIZATION_FAILURE";

            private static final String ANONYMOUS_USER = "anonymousUser";

            @Inject
            private AuditEventConverter auditEventConverter;

            @Override
            public List<AuditEvent> find(String principal, Date after) {
                Iterable<PersistentAuditEvent> persistentAuditEvents;
                if (principal == null && after == null) {
                    persistentAuditEvents = persistenceAuditEventRepository.findAll();
                } else if (after == null) {
                    persistentAuditEvents = persistenceAuditEventRepository.findByPrincipal(principal);
                } else {
                    persistentAuditEvents =
                        persistenceAuditEventRepository.findByPrincipalAndAuditEventDateAfter(principal,
                            after.getTime());
                }
                return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
            }

            @Override
            @Transactional(propagation = Propagation.REQUIRES_NEW)
            public void add(AuditEvent event) {
                if (!AUTHORIZATION_FAILURE.equals(event.getType()) &&
                    !ANONYMOUS_USER.equals(event.getPrincipal().toString())) {

                    PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent();
                    persistentAuditEvent.setPrincipal(event.getPrincipal());
                    persistentAuditEvent.setAuditEventType(event.getType());
                    Instant instant = Instant.ofEpochMilli(event.getTimestamp().getTime());
                    persistentAuditEvent.setAuditEventDDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));

                    Map<String, Object> data = event.getData();
                    Set<PersistentAuditEventData> results = new HashSet<>();

                    if (data != null) {
                        for (String key : data.keySet()) {
                            Object object = data.get(key);

                            // Extract the data that will be saved.
                            if (object instanceof WebAuthenticationDetails) {
                                WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails) object;
                                PersistentAuditEventData pad = new PersistentAuditEventData();
                                pad.setName("remoteAddress");
                                pad.setValue(authenticationDetails.getRemoteAddress());
                                pad = persistenceAuditEventDataRepository.save(pad);
                                results.add(pad);
                                PersistentAuditEventData pad2 = new PersistentAuditEventData();
                                pad2.setName("sessionId");
                                pad2.setValue(authenticationDetails.getSessionId());
                                pad2 = persistenceAuditEventDataRepository.save(pad2);
                                results.add(pad2);
                            } else {
                                PersistentAuditEventData pad = new PersistentAuditEventData();
                                pad.setName(key);
                                pad.setValue(object.toString());
                                pad = persistenceAuditEventDataRepository.save(pad);
                                results.add(pad);
                            }
                        }
                    }
                    persistentAuditEvent.setData(results);
                    persistenceAuditEventRepository.save(persistentAuditEvent);
                }
            }
        };
    }
}
