package uth.bio.ensembl.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import uth.bio.ensembl.model.PersistentToken;

public interface PersistentTokenRepository extends GraphRepository<PersistentToken> {

	PersistentToken findBySeries(String series);
	
	@Query("match (n:`User`{login:{0}})<-[:PERSISTENT_TOKENS]-(x)" +	           
	           "return x")
	Iterable<PersistentToken> getToken(String login);
	
}
