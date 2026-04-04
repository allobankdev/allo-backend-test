package com.allobank.allobank_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.allobank.allobank_api.config.ExternalApiProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExternalApiProperties.class)
public class AllobankApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllobankApiApplication.class, args);
	}

}
