package com.example.idr.rate.aggregator.fetcher;

import com.example.idr.rate.aggregator.dto.LatestIdrRatesDto;
import com.example.idr.rate.aggregator.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements IdrDataFetcher {

    private final WebClient webClient;
    private final double spreadFactor;

    public LatestIdrRatesFetcher(WebClient webClient,
                                 @Value("${app.spread.github-username}") String githubUsername) {
        this.webClient = webClient;
        this.spreadFactor = calculateSpread(githubUsername);
    }

    static double calculateSpread(String username) {
        int sum = 0;
        for (char c : username.toLowerCase().toCharArray()) sum += c;
        int mod = sum % 1000;
        return mod / 100000.0;
    }

    @Override
    public Mono<Object> fetch() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base","IDR").build())
                .retrieve()
                .onStatus(s -> !s.is2xxSuccessful(), resp -> Mono.error(new ExternalServiceException("failed latest")))
                .bodyToMono(LatestIdrRatesDto.class)
                .map(map -> {
                    Map<String, Object> rates = map.getRates();
                    Object usdObj = rates.get("USD");
                    double rateUsd = Double.parseDouble(usdObj.toString());
                    double usdBuySpread = (1.0 / rateUsd) * (1.0 + spreadFactor);

                    LatestIdrRatesDto dto = new LatestIdrRatesDto();
                    dto.setBase(map.getBase());
                    dto.setDate(map.getDate());
                    dto.setRates(rates);
                    dto.setUsdRate(rateUsd);
                    dto.setUsdBuySpreadIdr(BigDecimal.valueOf(usdBuySpread));
                    dto.setSpreadFactor(spreadFactor);
                    return dto;
                });
    }
}
