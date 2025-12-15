package com.allobank.frankfurter_aggregator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Frankfurter frankfurter = new Frankfurter();
    private Github github = new Github();
    
    @Data
    public static class Frankfurter {
        private String baseUrl;
        private Timeout timeout = new Timeout();
        
        @Data
        public static class Timeout {
            private int connect = 5000;
            private int read = 10000;
        }
    }
    
    @Data
    public static class Github {
        private String username;
    }
}
