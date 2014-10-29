package com.mycompany.myapp.config;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

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
import com.mycompany.myapp.web.rest.SecurityController;

@Configuration
@Transactional
public class Neo4JPopulator {

	private final Logger log = LoggerFactory
			.getLogger(SecurityController.class);

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
			Authority authAdmin = new Authority();
			authAdmin.setName(AuthoritiesConstants.ADMIN);
			authAdmin = authRepo.save(authAdmin);
			Authority authUser = new Authority();
			authUser.setName(AuthoritiesConstants.USER);
			authUser = authRepo.save(authUser);
			Set<Authority> authorities = new HashSet<Authority>();
			authorities.add(authUser);
			authorities.add(authAdmin);
			User user = new User();
			user.setActivated(true);
			user.setAuthorities(authorities);
			user.setEmail("panosvlastaridis@gmail.com");
			user.setFirstName("Panos");
			user.setLastName("Vlastaridis");
			user.setLogin("admin");
			user.setPassword("b8f57d6d6ec0a60dfe2e20182d4615b12e321cad9e2979e0b9f81e0d6eda78ad9b6dcfe53e4e22d1");
			user = userRepository.save(user);
		} 
		else {
			
			log.info("Admin user already in place skipping db population");
		}
		return new BeanPopulator();
	}

}

class BeanPopulator{}
