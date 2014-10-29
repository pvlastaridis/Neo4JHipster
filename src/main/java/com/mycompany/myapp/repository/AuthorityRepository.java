package com.mycompany.myapp.repository;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.mycompany.myapp.domain.Authority;

public interface AuthorityRepository extends GraphRepository<Authority> {


	Authority findByName(String name);
		
		
	}