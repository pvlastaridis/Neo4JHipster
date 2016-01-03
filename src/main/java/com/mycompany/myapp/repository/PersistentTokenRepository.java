package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PersistentToken;
import com.mycompany.myapp.domain.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

/**
 * Spring Data Neo4jDB repository for the PersistentToken entity.
 */
public interface PersistentTokenRepository extends GraphRepository<PersistentToken> {

    @Query("MATCH (n:PersistentToken)-[]->(:User {login:{0}}) RETURN n")
    List<PersistentToken> findByUserCypher(String login);

    @Query("MATCH (n:PersistentToken) WHERE n.tokenDate < {0} RETURN n")
    Iterable<PersistentToken> findByTokenDateBeforeCypher(Long localDate);

    PersistentToken findOneBySeries(String series);

}
