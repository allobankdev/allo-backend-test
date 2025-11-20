package com.athallah.finance.service.strategy;

import com.athallah.finance.client.FinanceFrankfurterWebClient;
import com.athallah.finance.dto.LatestRatesRawDto;
import com.athallah.finance.dto.LatestRatesResponseDto;
import com.athallah.finance.util.constant.ResourceType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("latest_idr_rates")
@Slf4j
public class LatestIdrRatesStrategy implements IDRDataFetcher {

    @Autowired
    private FinanceFrankfurterWebClient webClient;

    @Value("${finance.github.username}")
    private String githubUsername;

    private BigDecimal spreadFactor;

    @PostConstruct
    public void init() {
        this.spreadFactor = calculateSpreadFactor(githubUsername);
    }

    @Override
    public LatestRatesResponseDto fetchData() {

        LatestRatesRawDto rawData = webClient.getLatestIdrRates();

        return transformData(rawData);
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.latest_idr_rates;
    }

    private LatestRatesResponseDto transformData(LatestRatesRawDto rawData) {
        BigDecimal usdBuySpreadIdr = null;

        if (rawData.getRates() != null && rawData.getRates().containsKey("USD")) {
            BigDecimal usdRate = rawData.getRates().get("USD");

            usdBuySpreadIdr = BigDecimal.ONE
                    .divide(usdRate, 10, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.ONE.add(spreadFactor))
                    .setScale(8, RoundingMode.HALF_UP);
        }

        return LatestRatesResponseDto.builder()
                .amount(rawData.getAmount())
                .base(rawData.getBase())
                .date(rawData.getDate())
                .rates(rawData.getRates())
                .usdBuySpreadIdr(usdBuySpreadIdr)
                .build();
    }

    private BigDecimal calculateSpreadFactor(String username) {
        if (username == null || username.isEmpty()) {
            return new BigDecimal("0.00500");
        }

        String lowerUsername = username.toLowerCase();
        int unicodeSum = 0;

        for (char c : lowerUsername.toCharArray()) {
            unicodeSum += c;
        }

        int modResult = unicodeSum % 1000;

        return new BigDecimal(modResult)
                .divide(new BigDecimal("100000"), 5, RoundingMode.HALF_UP);
    }
}