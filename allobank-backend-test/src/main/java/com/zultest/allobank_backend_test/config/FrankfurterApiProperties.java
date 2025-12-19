package com.zultest.allobank_backend_test.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "external.api.frankfurter")
public class FrankfurterApiProperties {

    private String baseUrl;
    private int timeoutConnect;
    private int timeoutRead;
}
