package com.mycompany.myapp.config;


import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.typesafety.TypeSafetyOption;
import org.springframework.data.neo4j.support.typesafety.TypeSafetyPolicy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

@Configuration
@EnableNeo4jRepositories("com.mycompany.myapp.repository")
@EnableTransactionManagement
public class DatabaseConfiguration extends Neo4jConfiguration  {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

	public DatabaseConfiguration() {
		setBasePackage("com.mycompany.myapp.domain");
	}
		
	@Bean(destroyMethod = "shutdown")
	public GraphDatabaseService graphDatabaseService() {
		
		return new GraphDatabaseFactory()
				.newEmbeddedDatabase("data212/demo.db");
	}

	@Bean
	public Neo4jTemplate neo4jTemplate() {

		return new Neo4jTemplate(graphDatabaseService());
	}
	
	

	@PostConstruct
	public void initData() {
		this.log.error("Database set and running!"); 
	}
}