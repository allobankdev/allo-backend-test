package com.bezkoder.springjwt;

import com.bezkoder.springjwt.config.FrankfurterProperties;
import com.bezkoder.springjwt.config.GitHubProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FrankfurterProperties.class, GitHubProperties.class})
public class AlloBankFinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlloBankFinanceApplication.class, args);
    }
}
