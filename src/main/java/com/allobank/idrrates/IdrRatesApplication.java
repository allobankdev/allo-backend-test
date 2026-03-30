package com.allobank.idrrates;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IdrRatesApplication {

	private static final Logger log = LoggerFactory.getLogger(IdrRatesApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(IdrRatesApplication.class, args);
		log.info("Spring boot application started");
	}

}
