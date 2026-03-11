package com.allo.idraggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class IdrAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdrAggregatorApplication.class, args);
	}

}
