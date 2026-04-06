package com.allobank.idr_rate_aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class IdrRateAggregatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdrRateAggregatorApplication.class, args);
    }
}
