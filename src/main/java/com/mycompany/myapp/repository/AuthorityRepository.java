package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Authority;

import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Spring Data Neo4JDB repository for the Authority entity.
 */
public interface AuthorityRepository extends GraphRepository<Authority> {
	
	Authority findByName(String name);

}
