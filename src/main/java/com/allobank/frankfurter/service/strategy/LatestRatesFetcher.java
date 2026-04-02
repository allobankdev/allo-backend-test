package com.allobank.frankfurter.service.strategy;

import com.allobank.frankfurter.client.WebClientFactoryBean;
import com.allobank.frankfurter.model.DataResult;
import com.allobank.frankfurter.model.LatestRatesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Component
public class LatestRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final String githubUsername;
    private final String latestRatesPath;

    public LatestRatesFetcher(WebClientFactoryBean webClientFactoryBean,
                              @Value("${github.username}") String githubUsername,
                              @Value("${frankfurter.api.latest-rates-path}") String latestRatesPath) throws Exception {
        this.webClient = webClientFactoryBean.getObject();
        this.githubUsername = githubUsername;
        this.latestRatesPath = latestRatesPath;
    }

    @Override
    public DataResult fetchData() {
        LatestRatesResponse response = webClient.get()
                .uri(latestRatesPath)
                .retrieve()
                .bodyToMono(LatestRatesResponse.class)
                .block();

        BigDecimal rateUSD = response.getRates().get("USD");
        BigDecimal spreadFactor = calculateSpreadFactor();
        BigDecimal usdBuySpreadIdr = BigDecimal.ONE.divide(rateUSD, MathContext.DECIMAL64)
                .multiply(BigDecimal.ONE.add(spreadFactor))
                .setScale(2, RoundingMode.HALF_UP);

        Map<String, Object> result = new HashMap<>();
        result.put("base", response.getBase());
        result.put("date", response.getDate());
        result.put("rates", response.getRates());
        result.put("USD_BuySpread_IDR", usdBuySpreadIdr);

        return new DataResult(getResourceType(), result);
    }

    private BigDecimal calculateSpreadFactor() {
        long sum = githubUsername.toLowerCase().chars().sum();
        double spread = (sum % 1000) / 100000.0;
        return BigDecimal.valueOf(spread);
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }
}