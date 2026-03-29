package com.allobank.idrrates.strategy;

import com.allobank.idrrates.dto.LatestRatesDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class LatestRatesStrategy implements IdrDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(LatestRatesStrategy.class);
    @Autowired
    private WebClient webClient;

    @Override
    public String getResourceType() {
        return "latest_idr_rates";

    }

    @Override
    public Object fetchData() {
        log.info("Fetching latest IDR rates from Frankfurter API");
        return webClient.get()
                .uri("/rates/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRatesDTO.class)
                .block();
    }
}
