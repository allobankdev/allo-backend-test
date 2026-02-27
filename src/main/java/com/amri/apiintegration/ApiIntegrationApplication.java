package com.amri.apiintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ApiIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiIntegrationApplication.class, args);
    }

}
