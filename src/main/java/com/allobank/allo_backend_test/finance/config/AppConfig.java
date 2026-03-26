package com.allobank.allo_backend_test.finance.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Configuration
@Validated
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    @NotBlank
    private String githubUsername;

    @NotNull
    @Valid
    private PreloadConfig preload = new PreloadConfig();

    @NotNull
    @Valid
    private DataSourceConfig dataSource = new DataSourceConfig();

    @Data
    public static class PreloadConfig {
        @NotNull
        private Integer attempt;

        @NotNull
        private Long backoff;
    }

    @Data
    public static class DataSourceConfig {
        @NotBlank
        private String apiUrl;

        @NotNull
        private Integer connectTimeout;

        @NotNull
        private Integer readTimeout;
    }
}