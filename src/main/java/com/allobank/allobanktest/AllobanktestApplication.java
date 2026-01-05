package com.allobank.allobanktest;

import com.allobank.allobanktest.config.FrankfurterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(FrankfurterProperties.class)
@SpringBootApplication
public class AllobanktestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllobanktestApplication.class, args);
	}

}
