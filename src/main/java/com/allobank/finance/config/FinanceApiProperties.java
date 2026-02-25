package com.allobank.finance.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "finance")
public class FinanceApiProperties {

    private Api api = new Api();
    private Github github = new Github();

    @Data
    public static class Api {
        private String baseUrl;
        private int connectTimeout;
        private int readTimeout;
    }

    @Data
    public static class Github {
        private String username;
    }
}