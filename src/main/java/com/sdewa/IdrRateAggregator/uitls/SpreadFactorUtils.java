package com.sdewa.IdrRateAggregator.uitls;

import com.sdewa.IdrRateAggregator.constants.AppConstant;

public class SpreadFactorUtils {
    public static double calculateSpread(String githubUsername) {
        int sum = githubUsername.toLowerCase().chars().sum();
        return (sum % 1000) / 100_000.0;
    }

    public static double calculateUsdBuySpread(double rateUsd, String githubUsername) {
        double spread = calculateSpread(githubUsername);
        return (1 / rateUsd) * (1 + spread);
    }

    public static double calculateUsdBuySpread(double rateUsd) {
        return calculateUsdBuySpread(rateUsd, AppConstant.GITHUB_USERNAME);
    }
}
