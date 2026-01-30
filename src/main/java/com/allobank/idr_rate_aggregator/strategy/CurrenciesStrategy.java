package com.allobank.idr_rate_aggregator.strategy;

import com.allobank.idr_rate_aggregator.wrapper.CurrencyWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component("supported_currencies")
@RequiredArgsConstructor
public class CurrenciesStrategy implements DataFetcher{
    private final WebClient webClient;

    private List<CurrencyWrapper> cachedData = Collections.emptyList();

    @Override
    public List<CurrencyWrapper> fetchData() {
        return cachedData;
    }

    @Override
    public void refreshData() {
        List<CurrencyWrapper> tempList = new ArrayList<>();

        try {
            Map response = webClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();


            if (response != null) {
                Map<String, String> data = (Map<String, String>) response;
                for (String code : data.keySet()) {
                    String name = data.get(code);
                    tempList.add(new CurrencyWrapper(code, name));
                }
            }

            this.cachedData = tempList;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
