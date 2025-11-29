package com.htc.allobank;

import com.htc.allobank.config.ExternalApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExternalApiProperties.class)
public class AllobankApplication {
	public static void main(String[] args) {
		SpringApplication.run(AllobankApplication.class, args);
	}
}
