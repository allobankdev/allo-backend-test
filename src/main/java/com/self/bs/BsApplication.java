package com.self.bs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.self.bs.source.config.ExchangeRateProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExchangeRateProperties.class)
public class BsApplication {
	public static void main(String[] args) {
		SpringApplication.run(BsApplication.class, args);
	}
}
