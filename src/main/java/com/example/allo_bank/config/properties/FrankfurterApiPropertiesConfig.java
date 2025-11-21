package com.example.allo_bank.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class FrankfurterApiPropertiesConfig {

    @Value("${integration.api.frankfurter.base-url}")
    private String baseUrl;

    @Value("${integration.api.frankfurter.path.latest-idr-rates}")
    private String latestRatesPath;

    @Value("${integration.api.frankfurter.path.supported-currencies}")
    private String supportedCurrenciesPath;

    @Value("${integration.request.from-currency}")
    private String fromCurrency;

    @Value("${integration.request.to-currency}")
    private String toCurrency;

    @Value("${integration.request.start-date}")
    private String startDate;

    @Value("${integration.request.end-date}")
    private String endDate;

    public String getHistoricalPath() {
        return baseUrl + "/" + startDate + ".." + endDate + "?" + "from=" + fromCurrency + "&to=" + toCurrency;
    }

    public String getLatestIdrRatesPath() {
        return latestRatesPath + fromCurrency;
    }

}
