package id.co.microservice.currency.currency_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "external.api")
public class ExternalApiConfig {

    private String baseUrl;
    private int connectTimeout;
    private int readTimeout;

}
