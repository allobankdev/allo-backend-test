package com.thasya.frankfurter;

import com.thasya.frankfurter.config.FrankfurterClientProperties;
import com.thasya.frankfurter.config.GithubProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        GithubProperties.class,
        FrankfurterClientProperties.class
})
public class FrankfurterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrankfurterApplication.class, args);
    }
}

