package com.allobank.allobanktest.strategy;

import com.allobank.allobanktest.dto.LatestIdrRateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Slf4j
@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final String githubUsername;

    public LatestIdrRatesFetcher(
            WebClient webClient,
            @Value("${github.username}") String githubUsername
    ) {
        this.webClient = webClient;
        this.githubUsername = githubUsername;
    }

    @Override
    public String getResourceType() {
        return ResourceType.LATEST_IDR_RATES.getValue();
    }

    @Override
    public LatestIdrRateResponse fetchAndTransform() {
        log.info("Fetching latest IDR exchange rates");

        try {
            LatestIdrRateResponse apiResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/latest")
                            .queryParam("base", "IDR")
                            .build())
                    .retrieve()
                    .bodyToMono(LatestIdrRateResponse.class)
                    .block();

            if (apiResponse == null || apiResponse.rates() == null) {
                throw new IllegalStateException("Invalid response from Frankfurter API");
            }

            BigDecimal usdRate = apiResponse.rates().get("USD");
            if (usdRate == null) {
                throw new IllegalStateException("USD rate not found in response");
            }

            BigDecimal spreadFactor = calculateSpreadFactor(githubUsername);

            BigDecimal usdBuySpreadIdr =
                    BigDecimal.ONE
                            .divide(usdRate, 10, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.ONE.add(spreadFactor));

            log.info("Calculated USD buy spread using factor {}", spreadFactor);

            // IMPORTANT: create NEW immutable DTO
            return new LatestIdrRateResponse(
                    apiResponse.amount(),
                    apiResponse.base(),
                    apiResponse.date(),
                    apiResponse.rates(),
                    usdBuySpreadIdr
            );

        } catch (WebClientRequestException ex) {
            log.error("Network error while calling Frankfurter API", ex);
            throw ex; // fail fast

        } catch (Exception ex) {
            log.error("Failed to fetch latest IDR exchange rates", ex);
            throw ex;
        }
    }


    /**
     * Visible for testing
     */
    BigDecimal calculateSpreadFactor(String username) {
        int sum = username.toLowerCase()
                .chars()
                .sum();

        return new BigDecimal(sum % 1000)
                .divide(new BigDecimal("100000"), 10, RoundingMode.HALF_UP);
    }

}
