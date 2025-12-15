package com.allobank.frankfurter_aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class FrankfurterAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrankfurterAggregatorApplication.class, args);
	}

}
