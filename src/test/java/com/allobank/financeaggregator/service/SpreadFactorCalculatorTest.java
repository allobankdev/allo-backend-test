package com.allobank.financeaggregator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.allobank.financeaggregator.config.FrankfurterProperties;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SpreadFactorCalculatorTest {

    @Test
    void calculatesSpreadFactorFromUsername() {
        FrankfurterProperties properties = new FrankfurterProperties();
        properties.setGithubUsername("agilnurdiansah29");

        SpreadFactorCalculator calculator = new SpreadFactorCalculator(properties);
        BigDecimal spread = calculator.getSpreadFactor();

        assertThat(spread.compareTo(new BigDecimal("0.00589"))).isZero();
    }

    @Test
    void throwsWhenUsernameMissing() {
        FrankfurterProperties properties = new FrankfurterProperties();
        properties.setGithubUsername(" ");

        SpreadFactorCalculator calculator = new SpreadFactorCalculator(properties);

        assertThatThrownBy(calculator::getSpreadFactor)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("GitHub username");
    }
}
