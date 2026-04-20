package id.allobank.exchangerate.strategy;

import id.allobank.exchangerate.exception.ApiException;
import id.allobank.exchangerate.model.dto.LatestRatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LatestRatesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Value("${app.github-username}")
    private String username;

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {
        LatestRatesResponse response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .onStatus(status -> status.isError(), r ->
                        Mono.error(new RuntimeException("API Error")))
                .bodyToMono(LatestRatesResponse.class)
                .block();

        if (response == null) {
            throw new RuntimeException("Null response from API");
        }

        if (response.getRates() == null) {
            throw new ApiException("Rates data missing");
        }

        Double usdRate = response.getRates().get("USD");
        log.info("USD Rate: {}", usdRate);

        if (usdRate == null) {
            throw new RuntimeException("USD rate not found");
        }

        double spread = calculateSpread(username);
        log.info("Spread: {}", spread);

        double result = (1 / usdRate) * (1 + spread);

        response.setUSD_BuySpread_IDR(result);

        return response;
    }

    private double calculateSpread(String username) {
        int sum = username.chars().sum();
        return (sum % 1000) / 100000.0;
    }

}
