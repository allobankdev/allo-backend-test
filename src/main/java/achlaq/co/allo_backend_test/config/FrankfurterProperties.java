package achlaq.co.allo_backend_test.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter")
@Getter
@Setter
public class FrankfurterProperties {
    private String baseUrl;
    private int connectTimeoutMs;
    private int readTimeoutMs;
    private String historicalRange;
    private String githubUsername;
}