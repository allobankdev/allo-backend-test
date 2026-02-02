package com.example.allotest.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component("latest_idr_rates")
public class LatestRatesFetcher implements IDataFetcher {
    @Value("${github.username}")
    private String githubUsername;
    private final WebClient webClient;

    public LatestRatesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object[] fetchData() {
        Object[] obj = new Object[]{webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Object.class)
                .block()};

        LinkedHashMap<String, Double> valueFromObj = (LinkedHashMap<String, Double>) obj[0];
        Map<String, Object> ratesIntoMap = new HashMap<>();
        Map<String, Object> valueIntoMap = new HashMap<>();
        double usdRates = 0d;
        if (valueFromObj != null && valueFromObj.get("rates") != null) {
            valueIntoMap.put("amount", valueFromObj.get("amount"));
            valueIntoMap.put("base", valueFromObj.get("base"));
            valueIntoMap.put("date", valueFromObj.get("date"));
            ratesIntoMap.put("rates", valueFromObj.get("rates"));
            
            LinkedHashMap<String, Double> rates = (LinkedHashMap<String, Double>) ratesIntoMap.get("rates");
            Double usdRate = rates.get("USD");
            if (usdRate != null) {
                usdRates = usdRate;
            }
        }

        double spreadFactor = calculateSpreadFactor(githubUsername);
        double calculatedValue = calculateBuySpreadFromUSDtoIDR(spreadFactor, usdRates);

        Map<String, Object> result = new HashMap<>();
        result.put("amount", valueIntoMap.get("amount"));
        result.put("base", valueIntoMap.get("base"));
        result.put("date", valueIntoMap.get("date"));
        result.put("USD_BuySpread_IDR", calculatedValue);
        result.put("rates", ratesIntoMap.get("rates"));

        return new Map[]{result};
    }

    private static Double calculateSpreadFactor(String githubUsername) {
        int total = 0;
        for (char characterFromUsername : githubUsername.toLowerCase().toCharArray()) {
            total += characterFromUsername;
        }

        return (total % 1000) / 100000.0;
    }

    private static Double calculateBuySpreadFromUSDtoIDR(Double spreadFactor, Double usdRates) {
        return (1 / usdRates) * (1 + spreadFactor);
    }
}
