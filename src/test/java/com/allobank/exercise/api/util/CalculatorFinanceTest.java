package com.allobank.exercise.api.util;

import com.allobank.exercise.api.properties.GithubProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculatorFinanceTest {

    private GithubProperties githubProperties;
    private CalculatorFinance calculatorFinance;

    @BeforeEach
    void setUp() {
        githubProperties = mock(GithubProperties.class);
        calculatorFinance = new CalculatorFinance(githubProperties);
    }

    @Test
    void testGetSpreadFactor() {
        when(githubProperties.getUsername()).thenReturn("candra160391");
        // "candra160391" unicode sum = 925

        // expected calculation:
        // 925 * 1000 / 100000 = 9.25000

        BigDecimal result = calculatorFinance.getSpreadFactor();

        assertEquals(new BigDecimal("9.25000"), result);
    }

    @Test
    void testCalculateUSDBuySpreadIDR() {
        when(githubProperties.getUsername()).thenReturn("candra160391");

        BigDecimal rateUsd = new BigDecimal("0.00006");
        BigDecimal result = calculatorFinance.calculateUSDBuySpreadIDR(rateUsd);

        assertEquals(
                new BigDecimal("170833.33337"),
                result
        );
    }
}

