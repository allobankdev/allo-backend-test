package achlaq.co.allo_backend_test;

import achlaq.co.allo_backend_test.config.FrankfurterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(FrankfurterProperties.class)
@SpringBootApplication
public class AlloBackendTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlloBackendTestApplication.class, args);
	}

}
