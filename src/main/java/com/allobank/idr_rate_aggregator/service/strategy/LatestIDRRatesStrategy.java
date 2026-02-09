package com.allobank.idr_rate_aggregator.service.strategy;

import com.allobank.idr_rate_aggregator.model.ResourceType;
import com.allobank.idr_rate_aggregator.model.dto.LatestRatesResponse;
import com.allobank.idr_rate_aggregator.service.SpreadCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

/**
 * Strategy untuk mengambil latest IDR rates.
 *
 * Bertanggung jawab untuk:
 * - Memanggil endpoint /latest?base=IDR
 * - Menghitung USD_BuySpread_IDR
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LatestIDRRatesStrategy implements IDRDataFetcherStrategy {

    private final WebClient webClient;
    private final SpreadCalculatorService spreadCalculatorService;

    @Override
    public ResourceType getSupportedType() {
        return ResourceType.LATEST_IDR_RATES;
    }

    @Override
    public Object fetchData() {

        try {
            LatestRatesResponse response = webClient.get()
                    .uri("/latest?base=IDR")
                    .retrieve()
                    .bodyToMono(LatestRatesResponse.class)
                    .block();

            if (response == null || response.getRates() == null) {
                throw new IllegalStateException("Response latest rates kosong");
            }

            BigDecimal rateUsd = response.getRates().get("USD");

            if (rateUsd != null) {
                BigDecimal spread = spreadCalculatorService.calculateUsdBuySpread(rateUsd);
                response.setUsdBuySpreadIdr(spread);
            }

            return response;

        } catch (Exception e) {
            log.error("Gagal mengambil latest IDR rates", e);
            throw new RuntimeException("External API error - latest rates", e);
        }
    }
}