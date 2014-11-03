package com.mycompany.myapp.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.mycompany.myapp.domain.Author;

/**
 * Spring Data MongoDB repository for the Author entity.
 */
public interface AuthorRepository extends GraphRepository<Author> {
	
	@Query("MATCH (n:`Author`) WHERE id(n)={0} RETURN n")
	public Author findAuthorByID(Long id);

}
