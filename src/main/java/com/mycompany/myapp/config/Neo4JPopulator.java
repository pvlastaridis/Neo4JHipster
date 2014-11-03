package com.mycompany.myapp.config;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.web.rest.UserResource;

@Configuration
@Transactional
public class Neo4JPopulator {

	private final Logger log = LoggerFactory
			.getLogger(UserResource.class);

	@Inject
	private UserRepository userRepository;

	@Inject
	Neo4jTemplate neotemp;

	@Inject
	AuthorityRepository authRepo;

	@Bean
	public BeanPopulator populatedb() {

		if (userRepository.findByLogin("admin") == null) {
			log.info("Populating db");
			//Add two nodes for Authorities
			Authority authAdmin = new Authority();
			authAdmin.setName(AuthoritiesConstants.ADMIN);
			authAdmin = authRepo.save(authAdmin);
			Authority authUser = new Authority();
			authUser.setName(AuthoritiesConstants.USER);
			authUser = authRepo.save(authUser);
			//Admin User
			Set<Authority> authorities = new HashSet<Authority>();
			authorities.add(authUser);
			authorities.add(authAdmin);			
			User user = new User();
			user.setLogin("admin");
			user.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
			user.setFirstName("");
			user.setLastName("Administrator");
			user.setEmail("");
			user.setActivated(true);
			user.setLangKey("en");
			user.setCreatedBy("system");
			user.setCreatedDate(new DateTime());
			user.setAuthorities(authorities);
			user = userRepository.save(user);
			
			user = new User();
			user.setLogin("system");
			user.setPassword("$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG");
			user.setFirstName("");
			user.setLastName("System");
			user.setEmail("");
			user.setActivated(true);
			user.setLangKey("en");
			user.setCreatedBy("system");
			user.setCreatedDate(new DateTime());
			user.setAuthorities(authorities);
			user = userRepository.save(user);
			
			authorities = new HashSet<Authority>();
			user = new User();
			user.setLogin("anonymousUser");
			user.setPassword("$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO");
			user.setFirstName("Anonymous");
			user.setLastName("User");
			user.setEmail("");
			user.setActivated(true);
			user.setLangKey("en");
			user.setCreatedBy("system");
			user.setCreatedDate(new DateTime());
			user.setAuthorities(authorities);
			user = userRepository.save(user);
			
			authorities.add(authUser);
			user = new User();
			user.setLogin("user");
			user.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
			user.setFirstName("");
			user.setLastName("User");
			user.setEmail("");
			user.setActivated(true);
			user.setLangKey("en");
			user.setCreatedBy("system");
			user.setCreatedDate(new DateTime());
			user.setAuthorities(authorities);
			user = userRepository.save(user);
			
		} 
		else {			
			log.info("Users exist, skipping db population");
		}
		return new BeanPopulator();
	}

}

class BeanPopulator{}
