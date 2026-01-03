package com.allobank.allobackendtest.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.model.DTO.LatestIdrRatesResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LatestIdrRatesService {

    private static final String GITHUB_USERNAME = "fluxions-471";

    public LatestIdrRatesResponse applyUsdBuySpread(LatestIdrRatesResponse response) {

        BigDecimal usdRate = response.getRates().get("USD");

        BigDecimal spreadFactor = calculateSpreadFactor();

        log.info("Github Username ({}) Spread Factor = {}", GITHUB_USERNAME , spreadFactor);

        BigDecimal usdBuySpreadIdr = BigDecimal.ONE
                .divide(usdRate, 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(spreadFactor));

        response.setUsdBuySpreadIdr(usdBuySpreadIdr);
        return response;
    }

    BigDecimal calculateSpreadFactor() {
        int sum = GITHUB_USERNAME.chars().sum();
        return BigDecimal.valueOf(sum % 1000).divide(BigDecimal.valueOf(100_000), 5, RoundingMode.HALF_UP);
    }

}
