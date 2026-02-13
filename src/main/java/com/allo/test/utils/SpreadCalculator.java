package com.allo.test.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class SpreadCalculator {

    private static final BigDecimal ONE = BigDecimal.ONE;
    private static final BigDecimal THOUSAND = new BigDecimal("1000");
    private static final BigDecimal HUNDRED_THOUSAND = new BigDecimal("100000");

    private static final MathContext MC = new MathContext(16, RoundingMode.HALF_UP);

    public static BigDecimal calculateUsdBuySpreadIdr(
            String githubUsername,
            BigDecimal rateUsd
    ) {

//        System.out.println("username my github "+githubUsername);
//        System.out.println("rate usd "+rateUsd);

        if (githubUsername == null || githubUsername.isBlank()) {
            throw new IllegalArgumentException("GitHub username must not be empty");
        }

        if (rateUsd == null || rateUsd.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rate USD must be greater than 0");
        }

        String username = githubUsername.toLowerCase();

        int sum = username.chars().sum();

        BigDecimal spreadFactor = BigDecimal.valueOf(sum)
                .remainder(THOUSAND)
                .divide(HUNDRED_THOUSAND, MC);

        System.out.println("Spread factor for github username "+username+" is "+spreadFactor);


        BigDecimal invertedRate = ONE.divide(rateUsd, MC);

        return invertedRate
                .multiply(ONE.add(spreadFactor), MC)
                .setScale(6, RoundingMode.HALF_UP);
    }
}
