package com.mycompany.myapp.config;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.data.repository.query.QueryLookupStrategy;


@Configuration
@Profile("!test")
@EnableNeo4jRepositories(basePackages = "com.mycompany.myapp.repository", queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
public class DatabaseConfiguration extends Neo4jConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Override
    @Bean
    public Neo4jServer neo4jServer() {
        log.info("Initialising server connection");
        return new RemoteServer("http://localhost:7474", "neo4j", "1234");
    }

    @Override
    @Bean
    public SessionFactory getSessionFactory() {
        log.info("Initialising Session Factory");
        return new SessionFactory("com.mycompany.myapp.domain");
    }

    @Override
    @Bean
    public Session getSession() throws Exception {
        log.info("Initialising session-scoped Session Bean");
        return super.getSession();
    }
}
