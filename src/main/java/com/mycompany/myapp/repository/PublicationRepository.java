package com.mycompany.myapp.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.mycompany.myapp.domain.Publication;

/**
 * Spring Data MongoDB repository for the Publication entity.
 */
public interface PublicationRepository extends GraphRepository<Publication> {

}
