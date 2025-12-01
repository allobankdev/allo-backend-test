package com.example.idraggregator.service.strategy;

import com.example.idraggregator.config.FrankfurterClientFactoryBean;
import com.example.idraggregator.dto.LatestRatesDto;
import com.example.idraggregator.util.SpreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Fetches /latest?base=IDR and computes USD_BuySpread_IDR using SpreadUtil and the USD rate.
 */
@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher<LatestRatesDto> {

    private final WebClient webClient;
    private final String githubUsername;

    @Autowired
    public LatestIdrRatesFetcher(FrankfurterClientFactoryBean clientFactoryBean,
                                 org.springframework.core.env.Environment env) {
        // get the WebClient created by the FactoryBean
        this.webClient = (WebClient) clientFactoryBean.getObject();
        this.githubUsername = env.getProperty("personalization.github-username", "vivinessa");
    }

    @Override
    public LatestRatesDto fetch() throws Exception {
        // call frankfurter /latest?base=IDR
        Mono<LatestRatesDto> mono = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base", "IDR").build())
                .retrieve()
                .bodyToMono(LatestRatesDto.class);

        LatestRatesDto dto = mono.block(); // blocking during startup is acceptable
        if (dto == null) throw new IllegalStateException("No payload from frankfurter latest");

        Map<String, Double> rates = dto.getRates();
        Double usdRate = null;
        if (rates != null) {
            // In response when base=IDR, frankfurter returns rates like "USD": 0.000066...
            usdRate = rates.get("USD");
        }
        if (usdRate == null) {
            throw new IllegalStateException("USD rate not present in latest rates");
        }

        double spreadFactor = SpreadUtil.computeSpreadFactorFor(githubUsername);
        // Rate_USD is value from API when base=IDR. USD_BuySpread_IDR = (1 / Rate_USD) * (1 + SpreadFactor)
        double usdBuySpreadIdr = (1.0 / usdRate) * (1.0 + spreadFactor);
        dto.setUSD_BuySpread_IDR(usdBuySpreadIdr);
        return dto;
    }

    @Override
    public String resourceKey() {
        return "latest_idr_rates";
    }
}

