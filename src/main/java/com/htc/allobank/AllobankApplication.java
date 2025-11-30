package com.htc.allobank;

import com.htc.allobank.constant.Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(Module.CONFIGURATION)
public class AllobankApplication {
	public static void main(String[] args) {
		SpringApplication.run(AllobankApplication.class, args);
	}
}
