package com.allobank.exercise.api.util;

import com.allobank.exercise.api.properties.GithubProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CalculatorFinance {

    private final GithubProperties githubProperties;

    public CalculatorFinance(GithubProperties githubProperties) {
        this.githubProperties = githubProperties;
    }

    public BigDecimal calculateUSDBuySpreadIDR(BigDecimal rateUsd){
        BigDecimal spreadFactor = getSpreadFactor();
        return (BigDecimal.ONE.divide(rateUsd,5, RoundingMode.HALF_UP))
                .multiply((BigDecimal.ONE).add(spreadFactor)).setScale(5, RoundingMode.HALF_UP);
    }

    public BigDecimal getSpreadFactor(){
        String username = githubProperties.getUsername();
        int sumOfUnicode = username.chars().sum();

        return BigDecimal.valueOf(sumOfUnicode).multiply(BigDecimal.valueOf(1000)).divide(BigDecimal.valueOf(100000), 5, RoundingMode.UP);
    }
}
