package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PersistentToken;
import com.mycompany.myapp.domain.User;

import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

/**
 * Spring Data Neo4JDB repository for the PersistentToken entity.
 */
public interface PersistentTokenRepository extends GraphRepository<PersistentToken> {
	
	PersistentToken findBySeries(String series);		
	
    List<PersistentToken> findByTokenDateGreaterThan(Long date);
    
    List<PersistentToken> findByUser(User user);

}
