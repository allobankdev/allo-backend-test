package com.allobank.idrrates.strategy;

import com.allobank.idrrates.dto.CurrenciesDTO;
import com.allobank.idrrates.dto.TimeseriesRatesDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CurrenciesStrategy implements IdrDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(CurrenciesStrategy.class);
    @Autowired
    private WebClient webClient;

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchData() {
        log.info("Fetching supported currencies from Frankfurter API");
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(CurrenciesDTO.class)
                .block();
    }
}
