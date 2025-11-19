package com.allobank;

import com.allobank.config.properties.ClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ClientProperties.class})
public class AllobankApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllobankApplication.class, args);
	}

}
