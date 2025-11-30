package com.test.frankfurterexchangerate.service.implement;

import com.test.frankfurterexchangerate.dto.FrankfurterLatestDto;
import com.test.frankfurterexchangerate.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class FinanceServiceImpl implements FinanceService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "https://api.frankfurter.app";

    private final String githubUsername = "masbayuh";

    @Autowired
    public FinanceServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Map<String, Object>> fetchResource(String resourceType) {
        switch (resourceType) {
            case "latest_idr_rates":
                return fetchLatestIdrRates();
            case "historical_idr_use":
                return fetchHistoricalIdrUsd();
            case "support_currencies":
                return fetchSupportedCurrencies();
            default:
                throw new IllegalArgumentException("Unknown resource type: " + resourceType);
        }
    }

    private List<Map<String, Object>> fetchSupportedCurrencies() {
        String url = baseUrl + "/currencies";
        ResponseEntity<Map> resp = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> body = resp.getBody();
        List<Map<String, Object>> result = new ArrayList<>();

        if (body != null) {
            body.forEach((k, v) -> {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("currency", k);
                item.put("name", v);
                result.add(item);
            });
            result.sort(Comparator.comparing(m -> (String) m.get("currency")));
        }
        return result;
    }

    private List<Map<String, Object>> fetchHistoricalIdrUsd() {
        String url = baseUrl + "/2024-01-01..2024-01-05?from=IDR&to=USD";
        ResponseEntity<Map> resp = restTemplate.getForEntity(url, Map.class);
        Map body = resp.getBody();

        List<Map<String, Object>> result = new ArrayList<>();
        if(body != null && body.get("rates") instanceof Map) {
            Map<String, Map<String, Double>> rates = (Map) body.get("rates");
            for(Map.Entry<String, Map<String, Double>> entry : rates.entrySet()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("date", entry.getKey());
                item.put("rate_IDR_to_USD", entry.getValue().get("USD"));
                result.add(item);
            }

            result.sort(Comparator.comparing(m -> (String) m.get("date")));
        }

        return result;
    }

    private List<Map<String, Object>> fetchLatestIdrRates() {
        String url = baseUrl + "latest?base=IDR";
        ResponseEntity<FrankfurterLatestDto> resp = restTemplate.getForEntity(url, FrankfurterLatestDto.class);
        FrankfurterLatestDto dto = resp.getBody();

        Double rateUsd = null;
        if(dto != null && dto.getRates() != null) {
            rateUsd = dto.getRates().get("USD");
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("base", dto != null ? dto.getBase() : "IDR");
        out.put("date", dto != null ? dto.getDate() : null);
        out.put("rate", rateUsd);

        if(rateUsd != null && rateUsd != 0.0) {
            double spread = computeSpreadFactor(githubUsername);
            double usdBurSpreadIdr = (1.0 / rateUsd) * (1.0 + spread);
            out.put("USD_BuySpread_IDR", usdBurSpreadIdr);
            out.put("spread_factor", spread);
        } else {
            out.put("USD_BuySpread_IDR", null);
        }

        return Collections.singletonList(out);
    }

    private double computeSpreadFactor(String username) {
        String lower = username == null ? "" : username.toLowerCase(Locale.ROOT);
        int sum = 0;
        for (char c : lower.toCharArray()) sum += (int) c;
        int mod = sum % 1000;
        return mod / 100000.0;
    }
}
