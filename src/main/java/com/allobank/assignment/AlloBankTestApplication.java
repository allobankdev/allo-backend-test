package com.allobank.assignment;

import com.allobank.assignment.config.CurrencyApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CurrencyApiProperties.class)
public class AlloBankTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlloBankTestApplication.class, args);
	}
}
