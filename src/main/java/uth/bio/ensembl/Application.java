package uth.bio.ensembl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import uth.bio.ensembl.config.Neo4JConfig;

@ComponentScan
@EnableAutoConfiguration
public class Application {

	private static Class<Application> entryPointClass = Application.class;

	public static void main(String[] args) {
		SpringApplication.run(
				new Object[] { Neo4JConfig.class, entryPointClass }, args);
	}
}