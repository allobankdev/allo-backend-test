package com.hend.backend.strategy.impl;

import com.hend.backend.dto.FrankfurterResponse;
import com.hend.backend.dto.LatestIdrRatesResult;
import com.hend.backend.exception.ExternalApiException;
import com.hend.backend.strategy.IDRDataFetcher;
import com.hend.backend.util.SpreadCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author : hend wunga
 */

@Component("latest_idr_rates")
@RequiredArgsConstructor
public class LatestIdrRatesStrategy implements IDRDataFetcher {

    private final WebClient webClient;
    private final SpreadCalculator spreadCalculator;

    @Value("${app.github-username}")
    private String githubUsername;

    @Override
    public Object fetchData() {

        FrankfurterResponse response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        r -> Mono.error(new ExternalApiException("Frankfurter API error")))
                .bodyToMono(FrankfurterResponse.class)
                .timeout(Duration.ofSeconds(5))
                .block();

        if (response == null || !response.getRates().containsKey("USD")) {
            throw new ExternalApiException("USD rate not found");
        }

        double rateUsd = response.getRates().get("USD");
        double spreadFactor = spreadCalculator.calculateSpread(githubUsername);
        double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);

        return new LatestIdrRatesResult(response, usdBuySpreadIdr, spreadFactor);
    }


    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }
}
