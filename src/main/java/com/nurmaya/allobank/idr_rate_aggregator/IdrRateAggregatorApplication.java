package com.nurmaya.allobank.idr_rate_aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.nurmaya.allobank.idr_rate_aggregator.configuration.FrankfurterProperties;

@SpringBootApplication
@EnableConfigurationProperties(FrankfurterProperties.class)
public class IdrRateAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdrRateAggregatorApplication.class, args);
	}

}
