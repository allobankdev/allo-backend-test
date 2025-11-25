package com.example.financedata.fetcher;

import com.example.financedata.dto.LatestRatesDto;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;

import java.math.BigDecimal;
import java.util.Map;

import reactor.netty.http.client.HttpClient;
import java.time.Duration;


public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final String githubUsername;
    private final double spreadFactor;

    public LatestIdrRatesFetcher(WebClient webClient, String githubUsername) {
        //this.webClient = webClient;
        this.githubUsername = githubUsername.toLowerCase();
        this.spreadFactor = computeSpread(githubUsername.toLowerCase());

        HttpClient httpClient = HttpClient.create()
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) 
                .responseTimeout(Duration.ofSeconds(60)); 
                
        this.webClient = WebClient.builder()
                .baseUrl("https://api.frankfurter.app")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private static double computeSpread(String username) {
        int sum = username.chars().sum();
        return (sum % 1000) / 100000.0;
    }

    @Override
    public Mono<Object> fetch() {
        // call /latest?base=IDR
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base", "IDR").build())
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::transform);
    }

    private Object transform(Map<String, Object> raw) {
        // raw structure documented by Frankfurter: { "amount":1.0, "base":"IDR", "date":"YYYY-MM-DD", "rates":{ ... } }
        Map<String, Object> rates = (Map<String, Object>) raw.get("rates");
        Object usdRateObj = rates.get("USD");
        double rateUsd;
        if (usdRateObj instanceof Number) {
            rateUsd = ((Number) usdRateObj).doubleValue();
        } else {
            rateUsd = Double.parseDouble(usdRateObj.toString());
        }
        // Per spec Rate_USD is the value from API when base=IDR. Compute USD_BuySpread_IDR.
        double usdBuySpreadIdr = (1.0 / rateUsd) * (1.0 + spreadFactor);

        LatestRatesDto dto = new LatestRatesDto();
        dto.setBase((String) raw.get("base"));
        dto.setDate((String) raw.get("date"));
        dto.setRates(rates);
        dto.setUsdRate(rateUsd);
        dto.setUsdBuySpreadIdr(usdBuySpreadIdr);
        dto.setSpreadFactor(spreadFactor);
        return dto;
    }

    @Override
    public String resourceKey() {
        return "latest_idr_rates";
    }
}
