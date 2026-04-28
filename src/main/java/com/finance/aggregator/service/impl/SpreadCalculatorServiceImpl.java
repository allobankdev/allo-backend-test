package com.finance.aggregator.service.impl;

import com.finance.aggregator.service.SpreadCalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class SpreadCalculatorServiceImpl implements SpreadCalculatorService {

    private final BigDecimal spreadFactor;

    public SpreadCalculatorServiceImpl(@Value("${github.username}") String username) {
        this.spreadFactor = hitungSpreadFactor(username);
        log.info("Username: {}, Spread Factor: {}", username, spreadFactor);
    }

    private BigDecimal hitungSpreadFactor(String username) {
        String lower = username.toLowerCase();
        int sumUnicode = 0;

        for (char c : lower.toCharArray()) {
            sumUnicode += (int) c;
        }

        double factor = (sumUnicode % 1000) / 100000.0;
        return BigDecimal.valueOf(factor).setScale(5, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal hitungSpread(BigDecimal usdRate) {
        if (usdRate == null || usdRate.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Invalid USD rate");
        }

        BigDecimal inverseRate = BigDecimal.ONE.divide(usdRate, 10, RoundingMode.HALF_UP);
        BigDecimal hasil = inverseRate.multiply(BigDecimal.ONE.add(spreadFactor));

        return hasil.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getSpreadFactor() {
        return spreadFactor;
    }
}