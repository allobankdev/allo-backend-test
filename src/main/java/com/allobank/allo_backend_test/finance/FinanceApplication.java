package com.allobank.allo_backend_test.finance;

import com.allobank.allo_backend_test.finance.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FinanceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(FinanceApplication.class, args);

		AppConfig appConfig = context.getBean(AppConfig.class);
		System.out.println("App Config: " + appConfig);
	}

}
