package com.mycompany.myapp.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.mycompany.myapp.domain.User;

public interface UserRepository extends GraphRepository<User> {
	User findByLogin(String login);
	
	User findByActivationKey(String activationKey);
}

