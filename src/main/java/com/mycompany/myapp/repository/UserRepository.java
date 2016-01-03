package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.User;

import java.time.ZonedDateTime;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data Neo4jDB repository for the User entity.
 */
public interface UserRepository extends GraphRepository<User> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Long dateTime);

    User findOneByResetKey(String resetKey);

    User findOneByEmail(String email);

    User findOneByLogin(String login);

    User findOneById(String userId);

    @Override
    void delete(User t);

}
