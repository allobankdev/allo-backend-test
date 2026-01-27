package com.interview.backend.strategy.impl;

import com.interview.backend.models.ExchangeRateResponse;
import com.interview.backend.strategy.IDRDataFetcher;
import com.interview.backend.utils.RateFormatterUtil;
import com.interview.backend.utils.SpreadFactorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;

    @Value("${frankfurter.api.base-url:https://api.frankfurter.app}")
    private String baseUrl;

    @Value("${app.github.username:defaultuser}")
    private String defaultGithubUsername;

    @Override
    public Map<String, Object> fetchData() {
        try {
            String url = baseUrl + "/latest?base=IDR";

            ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);

            if (response == null || response.getRates() == null) {
                throw new RuntimeException("Failed to fetch latest rates - null response");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("base", response.getBase());
            result.put("date", response.getDate());
            result.put("rates", RateFormatterUtil.formatRatesMap(response.getRates()));

            Double usdRate = response.getRates().get("USD");
            if (usdRate != null) {
                double spreadFactor = SpreadFactorUtil.calculateSpreadFactor(defaultGithubUsername);
                double usdBuySpreadIDR = (1 / usdRate) * (1 + spreadFactor);
                result.put("USD_BuySpread_IDR", usdBuySpreadIDR);
                result.put("spread_factor", spreadFactor);
                result.put("github_username", defaultGithubUsername);
                result.put("usd_rate", usdRate);

            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch latest IDR rates: " + e.getMessage(), e);
        }
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }
}
