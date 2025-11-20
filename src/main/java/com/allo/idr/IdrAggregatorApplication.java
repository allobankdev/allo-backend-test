package com.allo.idr;

import com.allo.idr.config.ExternalApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExternalApiProperties.class)
public class IdrAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdrAggregatorApplication.class, args);
	}

}
