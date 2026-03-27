package com.allobank.allo_backend_test.finance;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
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


		DataSourceClient client = context.getBean(DataSourceClient.class);

		System.out.println("r");
		System.out.println(client.getLatestRates("IDR"));

		System.out.println("hist");
		System.out.println(client.getHistoricalRates("2024-01-01", "2024-01-05", "IDR", "USD"));

		System.out.println("curr");
		System.out.println(client.getCurrencies());
	}

}
