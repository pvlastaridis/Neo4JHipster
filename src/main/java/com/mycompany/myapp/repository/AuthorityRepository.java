package com.mycompany.myapp.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import com.mycompany.myapp.domain.Authority;

/**
 * Spring Data Neo4J DB repository for the Authority entity.
 */
public interface AuthorityRepository extends GraphRepository<Authority> {

	Authority findByName(String name);
}