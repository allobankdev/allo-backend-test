package com.example.allobank;

import com.example.allobank.config.FrankfurterApiProperties;
import com.example.allobank.config.GithubProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FrankfurterApiProperties.class, GithubProperties.class})
public class AllobankApplication {

    public static void main(String[] args) {
        SpringApplication.run(AllobankApplication.class, args);
    }
}