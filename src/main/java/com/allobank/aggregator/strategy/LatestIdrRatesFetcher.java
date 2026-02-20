package com.allobank.aggregator.strategy;

import com.allobank.aggregator.dto.FinanceDataDto;
import com.allobank.aggregator.dto.LatestRatesResponse;
import com.allobank.aggregator.util.SpreadCalculator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final String githubUsername;

    public LatestIdrRatesFetcher(WebClient webClient, @Value("${app.github.username}") String githubUsername) {
        this.webClient = webClient;
        this.githubUsername = githubUsername;
    }

    @Override
    public String resourceKey() {
        return "latest_idr_rates";
    }

    @Override
    public FinanceDataDto fetch() {
        LatestRatesResponse resp = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base", "IDR").build())
                .retrieve()
                .onStatus(s -> s.is4xxClientError() || s.is5xxServerError(),
                        clientResponse -> clientResponse.createException().flatMap(ex -> reactor.core.publisher.Mono.error(new RuntimeException("Frankfurter returned error: " + clientResponse.statusCode()))))
                .bodyToMono(LatestRatesResponse.class)
                .block();

        if (resp == null) {
            throw new RuntimeException("Empty latest response from Frankfurter");
        }

        Map<String, Object> out = new HashMap<>();
        out.put("base", resp.base());
        out.put("date", resp.date());

        Map<String, BigDecimal> rates = resp.rates();
        Map<String, Object> ratesWithExtra = new HashMap<>();

        double spreadFactor = SpreadCalculator.computeSpreadFactor(githubUsername);

        for (var entry : rates.entrySet()) {
            String currency = entry.getKey();
            BigDecimal rate = entry.getValue();
            Map<String, Object> detail = new HashMap<>();
            detail.put("rate", rate);

            if ("USD".equalsIgnoreCase(currency)) {
                BigDecimal usdBuySpreadIdr = BigDecimal.ZERO;
                if (rate != null && rate.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal inv = BigDecimal.ONE.divide(rate, MathContext.DECIMAL128);
                    usdBuySpreadIdr = inv.multiply(BigDecimal.valueOf(1.0 + spreadFactor), MathContext.DECIMAL128);
                }
                detail.put("USD_BuySpread_IDR", usdBuySpreadIdr);
                detail.put("spreadFactor", String.format("%.5f", spreadFactor));
            }

            ratesWithExtra.put(currency, detail);
        }

        out.put("rates", ratesWithExtra);
        return new FinanceDataDto(resourceKey(), out);
    }
}
