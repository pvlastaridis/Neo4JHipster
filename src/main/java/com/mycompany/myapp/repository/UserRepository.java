package com.mycompany.myapp.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.mycompany.myapp.domain.User;

public interface UserRepository extends GraphRepository<User> {
	
	User findByLogin(String login);
	
	User findByActivationKey(String activationKey);
	
	@Query("MATCH (`user`:`User`) WHERE `user`.`activated`= false  AND " +
	"`user`.`createdDate` <= {0} RETURN `user`")
    List<User> findNotActivatedUsersByCreationDateBefore(Long dateTime);

}

