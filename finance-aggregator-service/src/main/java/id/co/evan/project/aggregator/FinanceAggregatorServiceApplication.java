package id.co.evan.project.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("id.co.evan.project.aggregator")
public class FinanceAggregatorServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(FinanceAggregatorServiceApplication.class, args);
	}
}