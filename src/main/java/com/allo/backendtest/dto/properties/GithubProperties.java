package com.allo.backendtest.dto.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github")
public record GithubProperties(
        String username
) {
}
