package id.co.allobank.exchangerate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class ExchangeRateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRateServiceApplication.class, args);
	}

}
