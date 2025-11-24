package com.test.allo_bank_test_exhange_rate.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.test.allo_bank_test_exhange_rate.util.SpreadUtil;

import reactor.core.publisher.Mono;

@Service("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private String injectedUsername = new String(); // default

    public LatestIdrRatesFetcher(@Qualifier("frankfurterWebClientFactory") WebClient webClient) {
        this.webClient = webClient;
    }

    public void setGithubUsername(String githubUsername) {
        this.injectedUsername = githubUsername;
    }

    @Override
    public Mono<Object> fetchData() {
        Mono<Object> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base", "IDR").build())
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::transform);
        return result;
    }

    public Object transform(Map<String,Object> raw) {
        Map<String,Object> result = new HashMap<>(raw);
        Object ratesObj = raw.get("rates");

        if (ratesObj instanceof Map) {
            Map<?,?> rates = (Map<?,?>) ratesObj;
            Object usdObj = rates.get("USD");
            if (usdObj instanceof Number) {
                double rateUsd = ((Number) usdObj).doubleValue();
                double spread = SpreadUtil.calculateSpread(injectedUsername);
                double usdBuySpreadIdr = (1.0 / rateUsd) * (1.0 + spread);
                Map<String,Object> extra = new HashMap<>();
                extra.put("USD_BuySpread_IDR", usdBuySpreadIdr);
                result.put("computed", extra);
            }
        }
        return result;
    }
    
}
