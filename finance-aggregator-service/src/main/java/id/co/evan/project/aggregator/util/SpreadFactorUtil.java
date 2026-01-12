package id.co.evan.project.aggregator.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SpreadFactorUtil {

    public BigDecimal calculateSpreadFactor(String username) {
        if (username == null || username.isBlank()) return BigDecimal.ZERO.setScale(5, RoundingMode.HALF_UP);

        var sumUnicode = username.toLowerCase().chars().sum();

        return BigDecimal.valueOf(sumUnicode % 1000)
            .divide(BigDecimal.valueOf(100000), 5, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateBuySpread(double rateUsd, BigDecimal spreadFactor) {

        var inverseRate = BigDecimal.ONE.divide(BigDecimal.valueOf(rateUsd), 5, RoundingMode.HALF_UP);
        var valueOfFactor = BigDecimal.ONE.add(spreadFactor);

        return inverseRate.multiply(valueOfFactor).setScale(5, RoundingMode.HALF_UP);
    }
}