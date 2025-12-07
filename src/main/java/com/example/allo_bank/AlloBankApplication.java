package com.example.allo_bank;

import com.example.allo_bank.config.ApiClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication()
@EnableConfigurationProperties(ApiClientProperties.class)
public class  AlloBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlloBankApplication.class, args);
	}

}
