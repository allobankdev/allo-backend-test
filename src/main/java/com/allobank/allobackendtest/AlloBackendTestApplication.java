package com.allobank.allobackendtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.allobank.allobackendtest.config.FrankfurterProperties;

@SpringBootApplication
@EnableConfigurationProperties(FrankfurterProperties.class)
public class AlloBackendTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlloBackendTestApplication.class, args);
	}

}
