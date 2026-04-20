package id.allobank.exchangerate.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LatestRatesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {
        Map response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Double> rates = (Map<String, Double>) response.get("rates");

        double usdRate = rates.get("USD");

        double spread = calculateSpread("yourgithubusername");

        double result = (1 / usdRate) * (1 + spread);

        response.put("USD_BuySpread_IDR", result);

        return response;
    }

    private double calculateSpread(String username) {
        int sum = username.chars().sum();
        return (sum % 1000) / 100000.0;
    }

}
