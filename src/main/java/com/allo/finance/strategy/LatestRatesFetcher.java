package com.allo.finance.strategy;

import com.allo.finance.util.SpreadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class LatestRatesFetcher implements IDRDataFetcher {

    private final WebClient client;
    private final SpreadUtil spreadUtil;

    public LatestRatesFetcher(WebClient client, SpreadUtil spreadUtil) {
        this.client = client;
        this.spreadUtil = spreadUtil;
    }

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {

        Map res = client.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Double> rates = (Map<String, Double>) res.get("rates");

        Map<String, Object> formattedRates = new LinkedHashMap<>();
        rates.forEach((k, v) -> {
            BigDecimal bd = new BigDecimal(v.toString());
            formattedRates.put(k, bd);
        });

        res.put("rates", formattedRates);

        double usd = rates.get("USD");

        double spread = spreadUtil.calculateSpread();

        double calc = (1 / usd) * (1 + spread);

        BigDecimal spreadResult = new BigDecimal(calc)
                                        .setScale(2, RoundingMode.HALF_UP);

        res.put("USD_BuySpread_IDR", spreadResult);

        return res;
    }
}