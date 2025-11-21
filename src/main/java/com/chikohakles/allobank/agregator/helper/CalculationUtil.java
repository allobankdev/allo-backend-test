package com.chikohakles.allobank.agregator.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculationUtil {

    public static BigDecimal calculateSpreadFactor(String username) {
        int sum = 0;
        for (char c : username.toLowerCase().toCharArray()) {
            sum += c;
        }
        return new BigDecimal(sum % 1000).divide(new BigDecimal(100000));
    }

    public static BigDecimal calculateRate(BigDecimal spreadFactor, BigDecimal rate) {
        return BigDecimal.ONE
                .divide(rate, 5, RoundingMode.HALF_UP)
                .multiply(spreadFactor
                        .add(BigDecimal.ONE)
                );
    }
}
