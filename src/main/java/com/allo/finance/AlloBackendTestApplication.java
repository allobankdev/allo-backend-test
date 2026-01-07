package com.allo.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.allo.finance.config.FrankfurterProperties;

@EnableConfigurationProperties(FrankfurterProperties.class)
@SpringBootApplication
public class AlloBackendTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlloBackendTestApplication.class, args);
	}

}
