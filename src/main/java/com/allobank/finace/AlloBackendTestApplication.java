package com.allobank.finace;

import com.allobank.finace.properties.FrankfurterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FrankfurterProperties.class)
public class AlloBackendTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlloBackendTestApplication.class, args);
    }

}
