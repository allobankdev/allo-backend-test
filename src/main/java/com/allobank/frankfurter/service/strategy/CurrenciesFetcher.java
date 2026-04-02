package com.allobank.frankfurter.service.strategy;

import com.allobank.frankfurter.client.WebClientFactoryBean;
import com.allobank.frankfurter.model.DataResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class CurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final String currenciesPath;

    public CurrenciesFetcher(WebClientFactoryBean webClientFactoryBean,
                              @Value("${frankfurter.api.currencies-path}") String currenciesPath) throws Exception {
        this.webClient = webClientFactoryBean.getObject();
        this.currenciesPath = currenciesPath;
    }

    @Override
    public DataResult fetchData() {
        Map<String, String> currencies = webClient.get()
                .uri(currenciesPath)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return new DataResult(getResourceType(), currencies);
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }
}