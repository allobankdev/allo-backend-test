package com.allobanktest.idr.strategy;

import com.allobanktest.idr.dto.ExchangeRateSnapshot;
import com.allobanktest.idr.util.SpreadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

@Component("latest_idr_rates")
@RequiredArgsConstructor
public class LatestIdrRatesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Value("${app.github.username}")
    private String githubUsername;

    @Override
    public String key() {
        return "latest_idr_rates";
    }

    @Override
    public Mono<Map<String, Object>> fetchData() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base", "IDR").build())
                .retrieve()
                .bodyToMono(ExchangeRateSnapshot.class)
                .map(this::toResult)
                .onErrorResume(ex -> Mono.just(Map.of("error", ex.getMessage())));
    }

    private Map<String, Object> toResult(ExchangeRateSnapshot resp) {
        var rates = resp.getRates();
        if (rates == null || !rates.containsKey("USD") || rates.get("USD") == null) {
            return Map.of(
                    "date", resp.getDate(),
                    "base", resp.getBase(),
                    "rates", rates == null ? Map.of() : Map.copyOf(rates),
                    "error", "USD rate missing"
            );
        }

        BigDecimal usdRate = rates.get("USD");
        MathContext mc = new MathContext(20, RoundingMode.HALF_EVEN);
        BigDecimal inv = BigDecimal.ONE.divide(usdRate, mc);

        double spreadDouble = SpreadUtil.computeSpreadFactor(githubUsername);
        BigDecimal spreadFactor = BigDecimal.valueOf(spreadDouble);

        BigDecimal multiplier = BigDecimal.ONE.add(spreadFactor);
        BigDecimal usdBuySpread = inv.multiply(multiplier, mc);
        BigDecimal usdBuySpreadRounded = usdBuySpread.setScale(6, RoundingMode.HALF_EVEN);

        return Map.of(
                "date", resp.getDate(),
                "base", resp.getBase(),
                "rates", Map.copyOf(rates),
                "spreadFactor", spreadFactor,
                "USD_BuySpread_IDR", usdBuySpreadRounded
        );
    }
}
