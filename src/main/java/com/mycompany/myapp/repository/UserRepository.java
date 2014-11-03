package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.User;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

/**
 * Spring Data Neo4JDB repository for the User entity.
 */
public interface UserRepository extends GraphRepository<User> {
    
User findByLogin(String login);
	
	User findByActivationKey(String activationKey);
	
	@Query("MATCH (`user`:`User`) WHERE `user`.`activated`= false  AND " +
	"`user`.`createdDate` <= {0} RETURN `user`")
    List<User> findNotActivatedUsersByCreationDateBefore(Long dateTime);

}
