package com.allobank.finace.service.strategy;

import com.allobank.finace.properties.FrankfurterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("historical_idr_usd")
public class HistoricalIdrUsdStrategy implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "historical_idr_usd";

    private final FrankfurterProperties properties;

    @Autowired
    public HistoricalIdrUsdStrategy(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<Map<String, Object>> fetch(WebClient webClient) {
        String startDate = properties.getHistorical().getStartDate();
        String endDate = properties.getHistorical().getEndDate();
        String uri = "/" + startDate + ".." + endDate + "?from=IDR&to=USD";

        log.info("Fetching historical IDR/USD data from Frankfurter API: {}", uri);

        Map<String, Object> response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (response == null) {
            log.warn("Received null response for historical IDR/USD data");
            return List.of();
        }

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> ratesByDate =
                (Map<String, Map<String, Object>>) response.get("rates");

        if (ratesByDate == null) {
            return List.of();
        }

        List<Map<String, Object>> result = new ArrayList<>();
        ratesByDate.forEach((date, rates) -> {
            Map<String, Object> entry = new HashMap<>(rates);
            entry.put("date", date);
            result.add(entry);
        });

        result.sort((a, b) -> ((String) a.get("date")).compareTo((String) b.get("date")));

        return result;
    }
}
