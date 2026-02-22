package com.allobank.finnance.allobankfinance.util;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SpreadFactorCalculatorUtilTest {

    @Test
    void shouldCalculateSpreadFactorCorrectly() {
        // given
        String username = "herdiansyah5197";

        int asciiSum = username
                .toLowerCase()
                .chars()
                .sum();

        BigDecimal expected =
                BigDecimal.valueOf(asciiSum % 1000)
                        .divide(BigDecimal.valueOf(100000), 5, RoundingMode.HALF_UP);

        // when
        BigDecimal result =
                SpreadFactorCalculatorUtil.calculateSpreadFactor(username);

        // then
        AssertionsForClassTypes.assertThat(result)
                .isNotNull()
                .isEqualByComparingTo(expected);
    }

    @Test
    void shouldBeCaseInsensitive() {
        // given
        String lower = "herdiansyah5197";
        String upper = "HERDIANSYAH5197";

        // when
        BigDecimal lowerResult =
                SpreadFactorCalculatorUtil.calculateSpreadFactor(lower);

        BigDecimal upperResult =
                SpreadFactorCalculatorUtil.calculateSpreadFactor(upper);

        // then
        AssertionsForClassTypes.assertThat(upperResult)
                .isEqualByComparingTo(lowerResult);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        // then
        AssertionsForClassTypes.assertThatThrownBy(() ->
                        SpreadFactorCalculatorUtil.calculateSpreadFactor(null)
                )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("githubUsername must not be null or blank");
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsBlank() {
        // then
        AssertionsForClassTypes.assertThatThrownBy(() ->
                        SpreadFactorCalculatorUtil.calculateSpreadFactor(" ")
                )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("githubUsername must not be null or blank");
    }
}
