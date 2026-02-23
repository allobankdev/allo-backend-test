package com.allobank.finance.idrrateaggregator.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpreadCalculatorTest {

    @Test
    void shouldCalculateCorrectSpreadFactorForGivenUsername() {
        // given
        String username = "IlhamX7";

        // manual calculation:
        // ilhamx7 -> i(105)+l(108)+h(104)+a(97)+m(109)+x(120)+7(55) = 698
        // (698 % 1000) / 100000.0 = 0.00698
        double expected = 0.00698;

        // when
        double actual = SpreadCalculator.calculateSpreadFactor(username);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldBeCaseInsensitive() {
        // given
        String lower = "ilhamx7";
        String upper = "ILHAMX7";

        // when
        double lowerResult =
                SpreadCalculator.calculateSpreadFactor(lower);
        double upperResult =
                SpreadCalculator.calculateSpreadFactor(upper);

        // then
        assertThat(lowerResult).isEqualTo(upperResult);
    }

    @Test
    void spreadFactorShouldAlwaysBeBetweenZeroAndPointZeroZeroNineNineNine() {
        // given
        String username = "anyusername";

        // when
        double result =
                SpreadCalculator.calculateSpreadFactor(username);

        // then
        assertThat(result)
                .isGreaterThanOrEqualTo(0.0)
                .isLessThan(0.01);
    }
}
