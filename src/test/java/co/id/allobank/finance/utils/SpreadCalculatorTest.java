package co.id.allobank.finance.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SpreadCalculatorTest {

    private final String username = "dhimaspanji";

    @Test
    void shouldCalculateSpreadFactor() {
        double spread = SpreadCalculator.calculateSpread(username);

        assertTrue(spread >= 0);
        assertTrue(spread <= 0.00999);
    }

    @Test
    void shouldCalculateUsdBuySpread() {
        double result = SpreadCalculator.calculateUSDBuySpread(0.000064, username);

        assertTrue(result > 0);
    }
}
