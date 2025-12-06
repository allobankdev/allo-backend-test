package com.bank.allo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bank.allo")
public class AlloBankIdrAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlloBankIdrAggregatorApplication.class, args);
	}

}
