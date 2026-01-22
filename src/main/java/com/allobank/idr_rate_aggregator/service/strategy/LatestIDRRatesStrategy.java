package com.allobank.idr_rate_aggregator.service.strategy;

import com.allobank.idr_rate_aggregator.config.ApplicationProperties;
import com.allobank.idr_rate_aggregator.model.dto.FrankfurterLatestResponse;
import com.allobank.idr_rate_aggregator.model.dto.LatestIDRRatesResponse;
import com.allobank.idr_rate_aggregator.util.SpreadCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Strategy for fetching latest IDR exchange rates with spread calculation.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIDRRatesStrategy implements IDRDataFetcherStrategy {

    private final WebClient webClient;
    private final SpreadCalculator spreadCalculator;
    private final ApplicationProperties applicationProperties;

    @Override
    public Object fetchData() {
        log.info("Fetching latest IDR rates from Frankfurter API");
        
        try {
            // Call Frankfurter API /latest?base=IDR
            FrankfurterLatestResponse apiResponse = webClient.get()
                    .uri("/latest?base=IDR")
                    .retrieve()
                    .bodyToMono(FrankfurterLatestResponse.class)
                    .block(); // Blocking for simplicity, can be made reactive
            
            if (apiResponse == null) {
                throw new RuntimeException("Received null response from Frankfurter API");
            }
            
            // Enhance with spread calculation
            return enhanceWithSpreadCalculation(apiResponse);
            
        } catch (Exception e) {
            log.error("Error fetching latest IDR rates", e);
            throw new RuntimeException("Failed to fetch latest IDR rates: " + e.getMessage(), e);
        }
    }

    /**
     * Enhance API response with personalized spread calculation.
     */
    private LatestIDRRatesResponse enhanceWithSpreadCalculation(FrankfurterLatestResponse response) {
        // Get USD rate
        Double usdRate = response.getRates().get("USD");
        if (usdRate == null) {
            throw new RuntimeException("USD rate not found in latest rates response");
        }
        
        // Calculate spread factor from username
        String username = applicationProperties.getGithubUsername();
        double spreadFactor = spreadCalculator.calculateSpreadFactor(username);
        
        log.debug("Calculated spread factor: {} for username: {}", spreadFactor, username);
        
        // Calculate USD buy spread
        double usdBuySpread = spreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);
        
        log.info("Calculated USD_BuySpread_IDR: {}", usdBuySpread);
        
        // Build enhanced response
        return LatestIDRRatesResponse.builder()
                .rates(response.getRates())
                .base(response.getBase())
                .date(response.getDate())
                .usdBuySpreadIdr(usdBuySpread)
                .githubUsername(username)
                .build();
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }
}
