package com.allobank.idr_rate_aggregator.strategy;

import com.allobank.idr_rate_aggregator.wrapper.ChangeRateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author username github tengkuraafi44
 */
@Slf4j
@Component("latest_idr_rates")
@RequiredArgsConstructor
public class LatestRateStrategy implements DataFetcher {

    private final WebClient webClient;

    private static final String GITHUB_USERNAME = "tengkuraafi44";

    private List<ChangeRateWrapper> cachedData = Collections.emptyList();

    @Override
    public List<ChangeRateWrapper> fetchData() {
        return cachedData;
    }

    @Override
    public void refreshData() {
        try {
            BigDecimal rateIdrToUsd = BigDecimal.ZERO;
            BigDecimal buySpread = BigDecimal.ZERO;
            Map<String, BigDecimal> ratesMap = new HashMap<>();
            LocalDate date = LocalDate.now();
            Map response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/latest")
                            .queryParam("from", "IDR")
                            .queryParam("to", "USD")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("rates")) {
                if (response.containsKey("date")) {
                    date = LocalDate.parse(response.get("date").toString());
                }
                Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                Object rateValue = rates.get("USD");

                rateIdrToUsd = new BigDecimal(rateValue.toString());
                ratesMap.put("USD", rateIdrToUsd);

                BigDecimal spreadFactor = calculateSpreadFactor();

                BigDecimal formulaRate = BigDecimal.ONE.divide(rateIdrToUsd, MathContext.DECIMAL64);
                buySpread = formulaRate.multiply(BigDecimal.ONE.add(spreadFactor));
            }

            ChangeRateWrapper dto = ChangeRateWrapper.builder()
                    .base("IDR")
                    .target("USD")
                    .date(date)
                    .rates(ratesMap)
                    .buySpread(buySpread)
                    .build();

            this.cachedData = Collections.singletonList(dto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BigDecimal calculateSpreadFactor() {
        int sum = 0;
        for (char c : GITHUB_USERNAME.toLowerCase().toCharArray()) {
            sum += c;
        }

        double spreadFactor = sum % 1000;
        double buySpread = spreadFactor / 100000.0;
        log.info("Spread Factor {}", buySpread);
        return BigDecimal.valueOf(buySpread);
    }
}
