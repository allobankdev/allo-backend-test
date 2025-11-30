package com.test.frankfurterexchangerate.service.implement;

import com.test.frankfurterexchangerate.service.IDRDataFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.*;

//ToDo
@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {
    private final String baseUrl = "https://api.frankfurter.app";

    private final WebClient webClient;
    private final RestTemplate restTemplate;

    public SupportedCurrenciesFetcher(WebClient webClient, RestTemplate restTemplate) {
        this.webClient = webClient;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Object> fetchData() throws Exception {
        String url = baseUrl + "/currencies";
        ResponseEntity<Map> resp = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> body = resp.getBody();
        List<Object> result = new ArrayList<>();

        if (body != null) {
            body.forEach((k, v) -> {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("currency", k);
                item.put("name", v);
                result.add(item);
            });

            result.sort(Comparator.comparing(o -> {
                Map<String, Object> map = (Map<String, Object>) o;
                return (String) map.get("currency");
            }));
        }
        return result;
    }
}
