package com.mycompany.myapp.service;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.ApplicationTest;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.PersistentToken;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.repository.PersistentTokenRepository;
import com.mycompany.myapp.repository.UserRepository;
import java.time.ZonedDateTime;

import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.util.RandomUtil;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@ActiveProfiles("test")
@WebAppConfiguration
@IntegrationTest
public class UserServiceTest {

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private AuthorityRepository authRepo;

    @Before
    public void setup() {
        // Add two nodes for Authorities
        Authority authAdmin = new Authority();
        authAdmin.setName(AuthoritiesConstants.ADMIN);
        authAdmin = authRepo.save(authAdmin);
        Authority authUser = new Authority();
        authUser.setName(AuthoritiesConstants.USER);
        authUser = authRepo.save(authUser);
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

    }

    @Test
    public void testRemoveOldPersistentTokens() {
        User admin = userRepository.findOneByLogin("admin");
        int existingCount = persistentTokenRepository.findByUserCypher(admin.getLogin()).size();
        generateUserToken(admin, "1111-1111", LocalDate.now());
        LocalDate now = LocalDate.now();
        generateUserToken(admin, "2222-2222", now.minusDays(32));
        assertThat(persistentTokenRepository.findByUserCypher(admin.getLogin())).hasSize(existingCount + 2);
        userService.removeOldPersistentTokens();
        assertThat(persistentTokenRepository.findByUserCypher(admin.getLogin())).hasSize(existingCount + 1);
    }

    @Test
    public void assertThatUserMustExistToResetPassword() {
        Optional<User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
        assertThat(maybeUser.isPresent()).isFalse();

        maybeUser = userService.requestPasswordReset("admin@localhost");
        assertThat(maybeUser.isPresent()).isTrue();

        assertThat(maybeUser.get().getEmail()).isEqualTo("admin@localhost");
        assertThat(maybeUser.get().getResetDate()).isNotNull();
        assertThat(maybeUser.get().getResetKey()).isNotNull();
    }

    @Test
    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        User user = userService.createUserInformation("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US");
        Optional<User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
        assertThat(maybeUser.isPresent()).isFalse();
        userRepository.delete(user);
    }

    @Test
    public void assertThatResetKeyMustNotBeOlderThan24Hours() {
        User user = userService.createUserInformation("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US");

        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(25);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDDate(daysAgo);
        user.setResetKey(resetKey);

        userRepository.save(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());

        assertThat(maybeUser.isPresent()).isFalse();

        userRepository.delete(user);
    }

    @Test
    public void assertThatResetKeyMustBeValid() {
        User user = userService.createUserInformation("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US");

        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(25);
        user.setActivated(true);
        user.setResetDDate(daysAgo);
        user.setResetKey("1234");
        userRepository.save(user);
        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser.isPresent()).isFalse();
        userRepository.delete(user);
    }

    @Test
    public void assertThatUserCanResetPassword() {
        User user = userService.createUserInformation("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US");
        String oldPassword = user.getPassword();
        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(2);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.save(user);
        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser.isPresent()).isTrue();
        assertThat(maybeUser.get().getResetDate()).isNull();
        assertThat(maybeUser.get().getResetKey()).isNull();
        assertThat(maybeUser.get().getPassword()).isNotEqualTo(oldPassword);

        userRepository.delete(user);
    }

    @Test
    public void testFindNotActivatedUsersByCreationDateBefore() {
        userService.removeNotActivatedUsers();
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3).toEpochSecond());
        assertThat(users).isEmpty();
    }

    private void generateUserToken(User user, String tokenSeries, LocalDate localDate) {
        PersistentToken token = new PersistentToken();
        token.setSeries(tokenSeries);
        token.setUser(user);
        token.setTokenValue(tokenSeries + "-data");
        token.setTokenDate(localDate.toEpochDay());
        token.setIpAddress("127.0.0.1");
        token.setUserAgent("Test agent");
        persistentTokenRepository.save(token);
    }
}
