package com.mycompany.myapp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * This is an helper Java class that provides an alternative to creating a
 * web.xml.
 */
public class ApplicationWebXML extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.profiles().showBanner(false)
				.sources(Application.class);
	}

}