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
//import org.springframework.stereotype.Service;
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
			user.setPassword("b8f57d6d6ec0a60dfe2e20182d4615b12e321cad9e2979e0b9f81e0d6eda78ad9b6dcfe53e4e22d1");
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
			user.setPassword("b8f57d6d6ec0a60dfe2e20182d4615b12e321cad9e2979e0b9f81e0d6eda78ad9b6dcfe53e4e22d1");
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
			user.setPassword("4f54479f8290dfd503b72a654faf5d70593eab443993d87a79e14e5f7cda3eb7988423aa99090c9b");
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
			user.setPassword("4f54479f8290dfd503b72a654faf5d70593eab443993d87a79e14e5f7cda3eb7988423aa99090c9b");
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
