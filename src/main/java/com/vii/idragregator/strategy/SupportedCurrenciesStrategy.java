package com.vii.idragregator.strategy;

import com.vii.idragregator.dto.CurrencyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * @author Luthfi Aryarizki
 * @date Created on 2026/02/14 at 09:00 p.m
 */
@Slf4j
@Component("supported_currencies")
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    @Autowired
    private WebClient webClient;

    @Override
    public Object fetch() {
        log.info("Fetching supported currencies...");
        Map<String, String> rawData = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
        return new CurrencyDTO(rawData);
    }
}
