package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.ApplicationTest;
import com.mycompany.myapp.config.audit.AuditEventConverter;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.PersistentAuditEvent;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.repository.PersistenceAuditEventRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.AuditEventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AuditResource REST controller.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@ActiveProfiles("test")
@WebAppConfiguration
@IntegrationTest
public class AuditResourceTest {

    private static final String SAMPLE_PRINCIPAL = "SAMPLE_PRINCIPAL";
    private static final String SAMPLE_TYPE = "SAMPLE_TYPE";
    private static final LocalDateTime SAMPLE_TIMESTAMP = LocalDateTime.parse("2015-08-04T10:11:30");

    @Inject
    private UserRepository userRepository;

    @Inject
    private PersistenceAuditEventRepository auditEventRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private AuditEventConverter auditEventConverter;

    private PersistentAuditEvent auditEvent;

    private MockMvc restAuditMockMvc;


    @Before
    public void setup() {
        // Add two nodes for Authorities
        Authority authAdmin = new Authority();
        authAdmin.setName(AuthoritiesConstants.ADMIN);
        authAdmin = authorityRepository.save(authAdmin);
        Authority authUser = new Authority();
        authUser.setName(AuthoritiesConstants.USER);
        authUser = authorityRepository.save(authUser);
        // Admin User
        Set<Authority> authorities = new HashSet<Authority>();
        authorities.add(authUser);
        authorities.add(authAdmin);
        User user = new User();
        user.setLogin("admin");
        user.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        user.setFirstName("");
        user.setLastName("Administrator");
        user.setEmail("admin@localhost");
        user.setActivated(true);
        user.setLangKey("en");
        user.setCreatedDDate(ZonedDateTime.now());
        user.setAuthorities(authorities);
        user = userRepository.save(user);

        user = new User();
        user.setLogin("system");
        user.setPassword("$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG");
        user.setFirstName("");
        user.setLastName("System");
        user.setEmail("system@uth.gr");
        user.setActivated(true);
        user.setLangKey("en");
        user.setCreatedDDate(ZonedDateTime.now());
        user.setAuthorities(authorities);
        user = userRepository.save(user);

        authorities = new HashSet<Authority>();
        user = new User();
        user.setLogin("anonymousUser");
        user.setPassword("$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO");
        user.setFirstName("Anonymous");
        user.setLastName("User");
        user.setEmail("anonymousUser@uth.gr");
        user.setActivated(true);
        user.setLangKey("en");
        user.setCreatedDDate(ZonedDateTime.now());
        user.setAuthorities(authorities);
        user = userRepository.save(user);

        authorities.add(authUser);
        user = new User();
        user.setLogin("user");
        user.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
        user.setFirstName("");
        user.setLastName("User");
        user.setEmail("user@uth.gr");
        user.setActivated(true);
        user.setLangKey("en");
        user.setCreatedDDate(ZonedDateTime.now());
        user.setAuthorities(authorities);
        user = userRepository.save(user);

        MockitoAnnotations.initMocks(this);
        AuditEventService auditEventService =
                new AuditEventService(auditEventRepository, auditEventConverter);
        AuditResource auditResource = new AuditResource(auditEventService);
        this.restAuditMockMvc = MockMvcBuilders.standaloneSetup(auditResource).build();
    }

    @Before
    public void initTest() {
        auditEventRepository.deleteAll();
        auditEvent = new PersistentAuditEvent();
        auditEvent.setAuditEventType(SAMPLE_TYPE);
        auditEvent.setPrincipal(SAMPLE_PRINCIPAL);
        auditEvent.setAuditEventDDate(SAMPLE_TIMESTAMP);
    }


    @Test
    public void getAllAudits() throws Exception {
        // Initialize the database
        auditEventRepository.save(auditEvent);

        // Get all the audits
        restAuditMockMvc.perform(get("/api/audits"))
                .andExpect(status().isOk())
                // .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].principal").value(hasItem(SAMPLE_PRINCIPAL)));
    }

    @Test
    public void getAudit() throws Exception {
        // Initialize the database
        auditEventRepository.save(auditEvent);

        // Get the audit
        restAuditMockMvc.perform(get("/api/audits/{id}", auditEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.principal").value(SAMPLE_PRINCIPAL));
    }

    @Test
    public void getNonExistingAudit() throws Exception {
        // Get the audit
        restAuditMockMvc.perform(get("/api/audits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

}
