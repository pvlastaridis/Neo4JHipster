package uth.bio.ensembl.repository;
import org.springframework.data.neo4j.repository.GraphRepository;

import uth.bio.ensembl.model.Authority;

public interface AuthorityRepository extends GraphRepository<Authority> {


	Authority findByName(String name);
		
		
	}