package com.example.allo_bank.util;


import com.example.allo_bank.config.properties.GithubPropertiesConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CalculationTest {

    @Autowired
    private Calculation calculation;

    @MockitoBean
    private GithubPropertiesConfig githubPropertiesConfig;

    @Test
    void testGetSpreadFactor() {
        // Given (username diasumsikan 'abc')
        // 'a' = 97, 'b' = 98, 'c' = 99 → sum = 294
        when(githubPropertiesConfig.getUsername()).thenReturn("abc");

        // When
        BigDecimal spread = calculation.getSpreadFactor();

        // Then
        // mod = 294 % 1000 = 294
        // spreadFactor = 294 / 100000 = 0.00294
        assertThat(spread).isEqualByComparingTo(
                new BigDecimal("0.00294").setScale(5, RoundingMode.HALF_UP)
        );
    }

    @Test
    void testUsdBuySpreadIdr() {
        // Given username -> spreadFactor
        when(githubPropertiesConfig.getUsername()).thenReturn("abc"); // spread = 0.00294

        BigDecimal rateUsd = new BigDecimal("16000");

        // When
        BigDecimal result = calculation.usdBuySpreadIdr(rateUsd);

        // Manual calculation:
        // 1 / 16000 = 0.0000625 → scale 5 = 0.00006
        // 1 + 0.00294 = 1.00294
        // final = 0.00006 * 1.00294 = 0.0000602 → approx
        BigDecimal expected = new BigDecimal("0.00006")
                .multiply(new BigDecimal("1.00294"))
                .setScale(5, RoundingMode.HALF_UP);

        // Then
        assertThat(result).isEqualByComparingTo(expected);
    }

    @Test
    void testUsdBuySpreadIdrShouldCallGetSpreadFactor() {
        // Given
        when(githubPropertiesConfig.getUsername()).thenReturn("abc");

        // When
        calculation.usdBuySpreadIdr(new BigDecimal("15000"));

        // Then (verify method dipanggil)
        verify(githubPropertiesConfig).getUsername();
    }

}
