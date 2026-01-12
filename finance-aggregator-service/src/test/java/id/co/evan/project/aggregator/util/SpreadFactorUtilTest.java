package id.co.evan.project.aggregator.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpreadFactorUtilTest {

    private final SpreadFactorUtil util = new SpreadFactorUtil();

    @Test
    @DisplayName("Success - Calculate Spread Factor based on username")
    void shouldCalculateCorrectSpreadFactor() {
        var username = "evanahmad";
        var expected = BigDecimal.valueOf(0.00933);

        var actual = util.calculateSpreadFactor(username);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Success - Calculate Buy Spread")
    void shouldCalculateCorrectBuySpread() {
        var rateUsd = 0.000063;
        var spreadFactor = BigDecimal.valueOf(0.00933);

        var expected = BigDecimal.ONE.divide(BigDecimal.valueOf(rateUsd), 5, RoundingMode.HALF_UP)
            .multiply(BigDecimal.ONE.add(spreadFactor)).setScale(5, RoundingMode.HALF_UP);

        var actual = util.calculateBuySpread(rateUsd, spreadFactor);

        assertEquals(expected, actual);
    }
}