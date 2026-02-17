package com.exchange.frankfurter;

import com.exchange.frankfurter.config.FrankfurterProperties;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class FrankfurterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrankfurterApplication.class, args);
	}

	@Bean
	ApplicationRunner testRunner(FrankfurterProperties props) {
		return args -> {
			System.out.println("Base URL: " + props.getBaseUrl());
		};
	}

	@Bean
	ApplicationRunner testClient(WebClient client) {
		return args -> {
			System.out.println("WebClient initialized: " + client);
		};
	}

}
