package co.id.allobank.finance.utils;

public class SpreadCalculator {

    public static double calculateSpread(String username) {
        int sum = username.toLowerCase()
                .chars()
                .sum();

        return (sum % 1000) / 100000.0;
    }

    public static double calculateUSDBuySpread(double rateUsd, String username) {
        double spread = calculateSpread(username);

        return (1 / rateUsd) * (1 + spread);
    }
}
