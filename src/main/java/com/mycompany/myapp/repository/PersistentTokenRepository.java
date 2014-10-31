package com.mycompany.myapp.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.mycompany.myapp.domain.PersistentToken;
import com.mycompany.myapp.domain.User;

public interface PersistentTokenRepository extends GraphRepository<PersistentToken> {

	PersistentToken findBySeries(String series);
	
	@Query("match (n:`User`{login:{0}})<-[:PERSISTENT_TOKENS]-(x)" +	           
	           "return x")
	Iterable<PersistentToken> getToken(String login);
	
    List<PersistentToken> findByTokenDateGreaterThan(Long date);
    
    List<PersistentToken> findByUser(User user);
	
}
