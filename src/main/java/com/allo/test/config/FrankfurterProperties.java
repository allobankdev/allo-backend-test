package com.allo.test.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "external.frankfurter")
public class FrankfurterProperties {

    private String baseUrl;
    private Endpoints endpoints;
    private Timeout timeout;

    @Getter
    @Setter
    public static class Endpoints {
        private String latest;
        private String historical;
        private String currencies;
    }

    @Getter
    @Setter
    public static class Timeout {
        private int connect;
        private int read;
    }
}
