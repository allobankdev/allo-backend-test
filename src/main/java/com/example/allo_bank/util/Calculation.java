package com.example.allo_bank.util;

import com.example.allo_bank.config.properties.GithubPropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class Calculation {

    @Autowired
    private GithubPropertiesConfig githubPropertiesConfig;

    public BigDecimal getSpreadFactor() {

        String username = githubPropertiesConfig.getUsername();
        int sum = username.chars().sum();
        int mod = sum % 1000;
        BigDecimal spreadFactor = BigDecimal.valueOf(mod).divide(BigDecimal.valueOf(100000),5, RoundingMode.HALF_UP);

        return spreadFactor;

    }

    public BigDecimal usdBuySpreadIdr(BigDecimal rateUsd) {

        BigDecimal spreadFactor = getSpreadFactor();
        BigDecimal one = BigDecimal.ONE;
        BigDecimal result = one.divide(rateUsd, 5, RoundingMode.HALF_UP)
                .multiply(one.add(spreadFactor))
                .setScale(5, RoundingMode.HALF_UP);

        return result;

    }

}
