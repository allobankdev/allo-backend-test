package com.allobank.assignment;

import com.allobank.assignment.config.FrankfurterApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FrankfurterApiProperties.class)
public class IdrRateAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdrRateAggregatorApplication.class, args);
	}

}
