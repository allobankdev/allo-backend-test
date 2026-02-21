package com.allobank.finance.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "finance")
@Getter
@Setter
public class FinanceProperties {
    private String baseUrl;
    private String githubUsername;

}
