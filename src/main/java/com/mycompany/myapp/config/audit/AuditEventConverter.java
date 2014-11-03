package com.mycompany.myapp.config.audit;

import com.mycompany.myapp.domain.PersistentAuditEvent;
import com.mycompany.myapp.domain.PersistentAuditEventData;
import com.mycompany.myapp.repository.PersistenceAuditEventDataRepository;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.*;

import javax.inject.Inject;

@Configuration
public class AuditEventConverter {
	
	@Inject
    private PersistenceAuditEventDataRepository persistentAuditEventDataRepository;

    /**
     * Convert a list of PersistentAuditEvent to a list of AuditEvent
     * @param persistentAuditEvents the list to convert
     * @return the converted list.
     */
    public List<AuditEvent> convertToAuditEvent(Iterable<PersistentAuditEvent> persistentAuditEvents) {
        if (persistentAuditEvents == null) {
            return Collections.emptyList();
        }

        List<AuditEvent> auditEvents = new ArrayList<>();

        for (PersistentAuditEvent persistentAuditEvent : persistentAuditEvents) {
            AuditEvent auditEvent = new AuditEvent(persistentAuditEvent.getAuditEventDate().toDate(), persistentAuditEvent.getPrincipal(),
                    persistentAuditEvent.getAuditEventType(), convertDataToObjects(persistentAuditEvent.getData()));
            auditEvents.add(auditEvent);
        }

        return auditEvents;
    }

    /**
     * Internal conversion. This is needed to support the current SpringBoot actuator AuditEventRepository interface
     *
     * @param data the data to convert
     * @return a map of String, Object
     */
    public Map<String, Object> convertDataToObjects(Map<String, String> data) {
        Map<String, Object> results = new HashMap<>();

        if (data != null) {
            for (String key : data.keySet()) {
                results.put(key, data.get(key));
            }
        }

        return results;
    }

    /**
     * Internal conversion. This method will allow to save additional data.
     * By default, it will save the object as string
     *
     * @param data the data to convert
     * @return a map of String, String
     */
    public Set<PersistentAuditEventData> convertDataToStrings(Map<String, Object> data) {
        
    	Set<PersistentAuditEventData> results = new HashSet<>();
        if (data != null) {
            for (String key : data.keySet()) {
                Object object = data.get(key);
                // Extract the data that will be saved.
                if (object instanceof WebAuthenticationDetails) {
                    WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails) object;
                    PersistentAuditEventData paevd = new PersistentAuditEventData();
                    paevd.setName("remoteAddress");
                    paevd.setValue(authenticationDetails.getRemoteAddress());
                    paevd = persistentAuditEventDataRepository.save(paevd);
                    results.add(paevd);
                    paevd = new PersistentAuditEventData();
                    paevd.setName("sessionId");
                    paevd.setValue(authenticationDetails.getSessionId());
                    paevd = persistentAuditEventDataRepository.save(paevd);
                    results.add(paevd);
                } else {
                	PersistentAuditEventData paevd = new PersistentAuditEventData();
                    paevd.setName(key);
                    paevd.setValue(object.toString());
                    paevd = persistentAuditEventDataRepository.save(paevd);
                    results.add(paevd);
                }
            }
        }

        return results;
    }
}
