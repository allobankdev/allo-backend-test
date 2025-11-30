package com.test.frankfurterexchangerate.service.implement;

import com.test.frankfurterexchangerate.service.IDRDataFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.*;

//ToDo
@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {
    private final String baseUrl = "https://api.frankfurter.app";

    private final WebClient client;
    private final RestTemplate restTemplate;

    public HistoricalIdrUsdFetcher(WebClient client, RestTemplate restTemplate) {
        this.client = client;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Object> fetchData() throws Exception {
        String url = baseUrl + "/2024-01-01..2024-01-05?from=IDR&to=USD";
        ResponseEntity<Map> resp = restTemplate.getForEntity(url, Map.class);
        Map body = resp.getBody();

        List<Object> result = new ArrayList<>();
        if(body != null && body.get("rates") instanceof Map) {
            Map<String, Map<String, Double>> rates = (Map) body.get("rates");
            for(Map.Entry<String, Map<String, Double>> entry : rates.entrySet()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("date", entry.getKey());
                item.put("rate_IDR_to_USD", entry.getValue().get("USD"));
                result.add(item);
            }

            result.sort(Comparator.comparing(o -> {
                Map<String, Object> map = (Map<String, Object>) o;
                return LocalDate.parse((String) map.get("date"));
            }));
        }

        return result;
    }
}
