package org.allobanktest.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "frankfurter")
@Getter
@Setter
public class FrankfurterProperties {
    private String baseURL = "https://api.frankfurter.app";
    private int timeoutSeconds = 5;
}
