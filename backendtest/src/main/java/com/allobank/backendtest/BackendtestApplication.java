package com.allobank.backendtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.allobank.backendtest.config")
public class BackendtestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendtestApplication.class, args);
	}

}
