package com.mycompany.myapp.config;

//import java.io.File;
//import java.io.IOException;

import javax.annotation.PostConstruct;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
//import org.neo4j.kernel.impl.util.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.mycompany.myapp.repository")
@EnableTransactionManagement
public class DatabaseConfiguration extends Neo4jConfiguration {
	
	//private final Logger logger = LoggerFactory.getLogger(this.getClass());


	public DatabaseConfiguration() {
		
		setBasePackage("com.mycompany.myapp.domain");
		
		//Script to always delete Neo4J at startup
		/*try {
			
			// reset neo4jdb, this is a demo app...
			FileUtils.deleteRecursively(new File("data212/demo.db"));
			this.logger.info("Deleting Graph Database!!");

		} catch (IOException ex) {
			
			this.logger.error("Exception IO deleting Database: " + ex);
		}*/
	}

	@Bean(destroyMethod = "shutdown")
	public GraphDatabaseService graphDatabaseService() {
		
		return new GraphDatabaseFactory().newEmbeddedDatabase("data212/demo.db");
	}

	@Bean
	public Neo4jTemplate neo4jTemplate() {
		
		return new Neo4jTemplate(graphDatabaseService());
	}
	
	@PostConstruct
	public void initData() {
	}
}