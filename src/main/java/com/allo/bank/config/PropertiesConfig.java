package com.allo.bank.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    FrankfurterProperties.class,
    AppProperties.class
})
public class PropertiesConfig {
}
