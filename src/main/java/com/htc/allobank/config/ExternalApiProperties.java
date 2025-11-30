package com.htc.allobank.config;

import com.htc.allobank.constant.DefaultValues;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class ExternalApiProperties {
    private Frankfurter frankfurter = new Frankfurter();
    private Personalization personalization = new Personalization();

    @Getter
    @Setter
    public static class Frankfurter {
        private String baseUrl = DefaultValues.EMPTY_STRING;
        private int timeoutMs = DefaultValues.EMPTY_INT;
    }

    @Getter
    @Setter
    public static class Personalization {
        private String githubUsername = DefaultValues.EMPTY_STRING;
    }
}