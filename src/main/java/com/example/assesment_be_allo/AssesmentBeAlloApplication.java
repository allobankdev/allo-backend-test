package com.example.assesment_be_allo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

public class AssesmentBeAlloApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssesmentBeAlloApplication.class, args);
	}
}
