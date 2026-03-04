package com.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.finance.config.FrankfurterProperties;

@EnableConfigurationProperties(FrankfurterProperties.class)
@SpringBootApplication
public class FinanceApplication {

    public static void main(String[] args){
        SpringApplication.run(FinanceApplication.class, args);
    }
}
