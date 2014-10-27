package uth.bio.ensembl.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import uth.bio.ensembl.model.User;

public interface UserRepository extends GraphRepository<User> {
	User findByLogin(String login);
	
	User findByActivationKey(String activationKey);
}

