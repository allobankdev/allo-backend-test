package com.finance.aggregator.strategy;

import com.finance.aggregator.dto.LatestRatesDTO;
import com.finance.aggregator.service.SpreadCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestRatesStrategy implements DataFetcherStrategy {

    private final RestTemplate restTemplate;
    private final SpreadCalculatorService spreadCalculatorService;

    @Value("${external.api.frankfurter.url}")
    private String baseUrl;

    @Override
    public Mono<Object> fetch() {
        return Mono.fromCallable(() -> {
            String url = baseUrl + "/latest?base=IDR";
            log.info("Mengambil kurs terbaru IDR dari: {}", url);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || response.isEmpty()) {
                throw new RuntimeException("Tidak dapat mengambil data dari external API");
            }

            Map<String, Double> ratesDouble = (Map<String, Double>) response.get("rates");

            Map<String, BigDecimal> ratesBigDecimal = new HashMap<>();
            for (Map.Entry<String, Double> entry : ratesDouble.entrySet()) {
                ratesBigDecimal.put(entry.getKey(), BigDecimal.valueOf(entry.getValue()));
            }

            BigDecimal usdRate = ratesBigDecimal.get("USD");
            if (usdRate == null) {
                throw new RuntimeException("USD rate tidak ditemukan dalam response");
            }

            BigDecimal spread = spreadCalculatorService.hitungSpread(usdRate);

            return LatestRatesDTO.builder()
                    .base((String) response.get("base"))
                    .date((String) response.get("date"))
                    .rates(ratesBigDecimal)
                    .usdBuySpreadIdr(spread)
                    .resourceType(getType())
                    .build();
        });
    }

    @Override
    public String getType() {
        return "latest_idr_rates";
    }
}