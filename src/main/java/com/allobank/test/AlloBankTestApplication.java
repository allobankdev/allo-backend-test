package com.allobank.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */

@SpringBootApplication
public class AlloBankTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlloBankTestApplication.class, args);
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
		return builder -> {
			builder.featuresToDisable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			builder.featuresToEnable(com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
		};
	}
}
