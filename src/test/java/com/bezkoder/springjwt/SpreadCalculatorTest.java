package com.bezkoder.springjwt;

import com.bezkoder.springjwt.config.GitHubProperties;
import com.bezkoder.springjwt.util.SpreadCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpreadCalculatorTest {

    @Test
    void shouldComputeSpreadFactorDeterministically() {
        GitHubProperties props = new GitHubProperties();
        props.setUsername("johndoe47"); // example

        SpreadCalculator calc = new SpreadCalculator(props);

        // Sum ASCII
        int expectedSum = "johndoe47".chars().sum();
        double expectedSpread = (expectedSum % 1000) / 100000.0;

        assertEquals(expectedSpread, calc.spreadFactor(), 1e-12);
    }

    @Test
    void shouldComputeUsdBuySpreadIdr() {
        GitHubProperties props = new GitHubProperties();
        props.setUsername("abc"); // deterministic

        SpreadCalculator calc = new SpreadCalculator(props);

        double usdRate = 0.000064;
        double spread = calc.spreadFactor();
        double expected = (1.0 / usdRate) * (1.0 + spread);

        assertEquals(expected, calc.usdBuySpreadIdr(usdRate), 1e-9);
    }
}
