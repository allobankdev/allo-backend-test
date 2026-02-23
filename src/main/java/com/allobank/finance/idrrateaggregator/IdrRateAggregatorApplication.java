package com.allobank.finance.idrrateaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class IdrRateAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdrRateAggregatorApplication.class, args);
	}

}
