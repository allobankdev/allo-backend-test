package achlaq.co.allo_backend_test.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public final class SpreadFactorCalculator {

    private SpreadFactorCalculator() {}

    public static BigDecimal calculateSpreadFactor(String githubUsername) {
        String lower = githubUsername.toLowerCase(Locale.ROOT);
        int sum = 0;
        for (char c : lower.toCharArray()) {
            sum += (int) c;
        }
        BigDecimal numerator = BigDecimal.valueOf(sum % 1000L);
        return numerator.divide(BigDecimal.valueOf(100_000L), 5, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateUsdBuySpreadIdr(BigDecimal rateUsd, BigDecimal spreadFactor) {
        BigDecimal one = BigDecimal.ONE;
        BigDecimal idrPerUsd = one.divide(rateUsd, 8, RoundingMode.HALF_UP);
        return idrPerUsd.multiply(one.add(spreadFactor))
                .setScale(4, RoundingMode.HALF_UP);
    }
}
