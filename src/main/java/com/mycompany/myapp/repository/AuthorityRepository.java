package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Authority;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Spring Data Neo4jDB repository for the Authority entity.
 */
public interface AuthorityRepository extends GraphRepository<Authority> {

    Authority findOneByName(String name);
}
