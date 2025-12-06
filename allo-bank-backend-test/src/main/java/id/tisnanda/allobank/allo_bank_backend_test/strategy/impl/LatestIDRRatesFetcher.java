package id.tisnanda.allobank.allo_bank_backend_test.strategy.impl;

import id.tisnanda.allobank.allo_bank_backend_test.exception.BadRequestException;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.IDRDataFetcher;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("latest_idr_rates")
@Setter
public class LatestIDRRatesFetcher implements IDRDataFetcher {

    @Autowired
    public RestTemplate restTemplate;

    @Value("${external.frankfurter.latest-url}")
    public String latestUrl;

    @Value("${spread.github-username}")
    public String githubUsername;

    @Override
    public List<Map<String, Object>> fetchData() {
        if (restTemplate == null) {
            throw new BadRequestException("RestTemplate must be set before fetching data");
        }

        Map<String, Object> response = restTemplate.getForObject(latestUrl, Map.class);

        if (response == null || !response.containsKey("rates")) {
            throw new BadRequestException("Failed to fetch latest IDR rates");
        }

        Map<String, Object> rates = (Map<String, Object>) response.get("rates");

        double spreadFactor = calculateSpreadFactor(githubUsername);

        Map<String, Object> transformed = new HashMap<>(rates);

        if (rates.containsKey("USD")) {
            double rateUSD = ((Number) rates.get("USD")).doubleValue();
            double usdBuySpread = (1 / rateUSD) * (1 + spreadFactor);
            transformed.put("USD_BuySpread_IDR", usdBuySpread);
        }

        return Collections.singletonList(transformed);
    }

    private double calculateSpreadFactor(String username) {
        username = username.toLowerCase();
        int sum = username.chars().sum();
        return (sum % 1000) / 100000.0;
    }

}
