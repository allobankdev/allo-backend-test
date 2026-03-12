package com.allobank.finace.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "frankfurter")
public class FrankfurterProperties {

    private String baseUrl;
    private Historical historical = new Historical();

    @Getter
    @Setter
    public static class Historical {
        private String startDate;
        private String endDate;
    }
}
