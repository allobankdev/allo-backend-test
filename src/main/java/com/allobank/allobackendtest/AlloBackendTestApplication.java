package com.allobank.allobackendtest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AlloBackendTestApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AlloBackendTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("AlloBackendTestApplication - START");

        log.info("AlloBackendTestApplication - END");
    }

}
