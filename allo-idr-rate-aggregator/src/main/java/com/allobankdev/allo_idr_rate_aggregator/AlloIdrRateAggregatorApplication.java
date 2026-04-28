package com.allobankdev.allo_idr_rate_aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.allobankdev.allo_idr_rate_aggregator.config.FrankfurterProperties;

@SpringBootApplication
@EnableConfigurationProperties(FrankfurterProperties.class)
public class AlloIdrRateAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlloIdrRateAggregatorApplication.class, args);
	}

}
