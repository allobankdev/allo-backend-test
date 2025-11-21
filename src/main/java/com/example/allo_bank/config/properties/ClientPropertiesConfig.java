package com.example.allo_bank.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "client")
public class ClientPropertiesConfig {

    private Integer connectionTimeout;
    private Integer readTimeout;

}
