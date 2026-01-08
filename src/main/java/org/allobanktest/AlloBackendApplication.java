package org.allobanktest;

import org.allobanktest.client.FrankfurterClientFactory;
import org.allobanktest.client.FrankfurterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(FrankfurterProperties.class)
public class AlloBackendApplication {
    @Bean(name = "frankfurterClientFactory")
    public FrankfurterClientFactory frankfurterClientFactory(FrankfurterProperties props) {
        return new FrankfurterClientFactory(props);
    }

    public static void main(String[] args) {
        SpringApplication.run(AlloBackendApplication.class, args);
    }

}
