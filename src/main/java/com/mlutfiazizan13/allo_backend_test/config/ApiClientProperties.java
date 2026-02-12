package com.mlutfiazizan13.allo_backend_test.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api.client")
@Getter
@Setter
public class ApiClientProperties {
    private String baseUrl;
    private int connectionTimeout = 5000;
    private int readTimeout = 5000;
    private String apiKey;
    private Headers headers = new Headers();

    public static class Headers {
        private String accept = "application/json";
        private String contentType = "application/json";

        public String getAccept() { return accept; }
        public void setAccept(String accept) { this.accept = accept; }

        public String getContentType() { return contentType; }
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }
}
