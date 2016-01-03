package com.mycompany.myapp;

import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.config.JHipsterProperties;

import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@ComponentScan
@EnableAutoConfiguration(exclude = { MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class })
@EnableConfigurationProperties(JHipsterProperties.class)
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Inject
    private Environment env;

    @Inject
    AuthorityRepository authRepo;

    @Inject
    UserRepository userRepository;

    /**
     * Initializes neo4jhipster.
     * <p/>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     * <p/>
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="http://jhipster.github.io/profiles.html">http://jhipster.github.io/profiles.html</a>.
     * </p>
     */
    @PostConstruct
    public void initApplication() throws IOException {
        if (env.getActiveProfiles().length == 0) {
            log.warn("No Spring profile configured, running with default configuration");
        } else {
            log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
            Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
            if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
                log.error("You have misconfigured your application! " +
                    "It should not run with both the 'dev' and 'prod' profiles at the same time.");
            }
            if (activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION) && activeProfiles.contains(Constants.SPRING_PROFILE_FAST)) {
                log.error("You have misconfigured your application! " +
                    "It should not run with both the 'prod' and 'fast' profiles at the same time.");
            }
            if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(Constants.SPRING_PROFILE_CLOUD)) {
                log.error("You have misconfigured your application! " +
                    "It should not run with both the 'dev' and 'cloud' profiles at the same time.");
            }
        }
        if (userRepository.findOneByLogin("admin") == null) {
            log.info("Populating db");
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
            user.setEmail("admin@uth.gr");
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

        } else {
            log.info("Users exist, skipping db population");
        }
    }

    /**
     * Main method, used to run the application.
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(Application.class);
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
        addDefaultProfile(app, source);
        Environment env = app.run(args).getEnvironment();
        log.info("Access URLs:\n----------------------------------------------------------\n\t" +
                "Local: \t\thttp://127.0.0.1:{}\n\t" +
                "External: \thttp://{}:{}\n----------------------------------------------------------",
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"));

    }

    /**
     * If no profile has been configured, set by default the "dev" profile.
     */
    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active") &&
                !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {

            app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
        }
    }
}
