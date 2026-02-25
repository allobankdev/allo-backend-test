package com.allobank.finance;

import com.allobank.finance.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.allobank.finance.config.FrankfurterProperties;

@SpringBootApplication
@EnableConfigurationProperties({FrankfurterProperties.class, AppProperties.class})
public class FinanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class, args);
    }
}
