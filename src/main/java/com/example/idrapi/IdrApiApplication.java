package com.example.idrapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class IdrApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdrApiApplication.class, args);
    }
}
