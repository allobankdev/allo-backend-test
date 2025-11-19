package com.allo.test.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "frankfurter.api")
public class FrankfurterApiProperties {

    private String baseUrl;

    private Integer connectionTimeout;

    private Integer readTimeout;

    private Integer writeTimeout;

    private Integer maxInMemorySize;

    private String githubUsername;

    private LatestRatesConfig latestRates;

    private HistoricalRatesConfig historicalRates;

    private CurrenciesConfig currencies;

    @Data
    public static class LatestRatesConfig {
        private String endpoint;
        private String baseCurrency;
    }

    @Data
    public static class HistoricalRatesConfig {
        private String dateRange;
        private String fromCurrency;
        private String toCurrency;
    }

    @Data
    public static class CurrenciesConfig {
        private String endpoint;
    }

}
