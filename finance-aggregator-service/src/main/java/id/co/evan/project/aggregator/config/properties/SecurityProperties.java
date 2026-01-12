package id.co.evan.project.aggregator.config.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.security")
public record SecurityProperties(
    @NotBlank
    String apiKey
) { }
