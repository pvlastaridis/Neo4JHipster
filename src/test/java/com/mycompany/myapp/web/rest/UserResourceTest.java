package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserResourceTest {

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserService userService;

    private MockMvc restUserMockMvc;

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

        UserResource userResource = new UserResource();
        ReflectionTestUtils.setField(userResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(userResource, "userService", userService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
    }

    @Test
    public void testGetExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/admin")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.lastName").value("Administrator"));
    }

    @Test
    public void testGetUnknownUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
