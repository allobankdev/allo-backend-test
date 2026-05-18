package com.allo.splitbill.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ServiceChargeCalculator {

    private final int serviceChargePct;

    public ServiceChargeCalculator(@Value("${github.username}") String githubUsername) {
        this.serviceChargePct = computeServiceChargePct(githubUsername);
    }

    public int getServiceChargePct() {
        return serviceChargePct;
    }

    public BigDecimal calculateServiceChargeAmount(BigDecimal totalExpenses) {
        return totalExpenses.multiply(BigDecimal.valueOf(serviceChargePct))
                .divide(BigDecimal.valueOf(100));
    }

    static int computeServiceChargePct(String username) {
        String lower = username.toLowerCase();
        int sum = 0;
        for (char c : lower.toCharArray()) {
            sum += c;
        }
        return sum % 10;
    }
}
