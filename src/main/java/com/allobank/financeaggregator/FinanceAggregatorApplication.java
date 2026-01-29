package com.allobank.financeaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FinanceAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceAggregatorApplication.class, args);
	}

}
