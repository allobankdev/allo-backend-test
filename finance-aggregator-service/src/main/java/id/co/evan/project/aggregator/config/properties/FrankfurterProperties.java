package id.co.evan.project.aggregator.config.properties;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Role;

@ConfigurationProperties(prefix = "api.frankfurter")
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public record FrankfurterProperties(
    String baseUrl,
    int connectTimeoutMs,
    int responseTimeoutMs
) { }