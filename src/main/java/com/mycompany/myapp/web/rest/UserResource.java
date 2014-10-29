package com.mycompany.myapp.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/app")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    Neo4jTemplate neotemp;
    
	@Autowired
	AuthorityRepository authRepo;
	

    /**
     * GET  /rest/users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/rest/users/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public User getUser(@PathVariable String login, HttpServletResponse response) {
        log.debug("REST request to get User : {}", login);
        User user = neotemp.findByIndexedValue(User.class, "login", login).singleOrNull();
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return user;
    }
    
}