package com.allobankdev.split_bill_api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ServiceChargeUtil {

    private static final String GITHUB_USERNAME =
            "jokokusnandi";

    public static int calculatePercentage() {

        int sum = GITHUB_USERNAME
                .chars()
                .sum();

        return sum % 10;
    }

    public static BigDecimal calculateAmount(
            BigDecimal totalExpense) {

        int pct = calculatePercentage();

        return totalExpense
                .multiply(BigDecimal.valueOf(pct))
                .divide(BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP);
    }
}
