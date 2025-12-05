package achlaq.co.allo_backend_test.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter")
public class FrankfurterProperties {
    private String baseUrl;
    private int connectTimeoutMs;
    private int readTimeoutMs;
    private String historicalRange;
    private String githubUsername;
}