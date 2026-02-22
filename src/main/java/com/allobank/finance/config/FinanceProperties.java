package com.allobank.finance.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "finance")
@Data
public class FinanceProperties {
    private String baseUrl;
    private String githubUsername;

}
