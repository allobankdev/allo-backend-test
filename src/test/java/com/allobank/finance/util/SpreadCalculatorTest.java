package com.allobank.finance.util;

import com.allobank.finance.config.properties.GithubProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpreadCalculatorTest {

    @InjectMocks
    SpreadCalculator calculator;

    @Mock
    GithubProperties properties;

    @Test
    void shouldCalculateSpreadFactorCorrectly() {
        var username = "johndoe";
        Mockito.when(properties.username()).thenReturn(username);

        var sum = username.chars().sum();
        var expected = (sum % 1000) / 100000.0;

        var result = calculator.calculateSpreadFactor();

        Assertions.assertEquals(expected, result);
    }

    @Test
    void shouldReturnZeroWhenUsernameCharsSumDivisibleBy1000() {
        var username = "d";
        Mockito.when(properties.username()).thenReturn(username);

        var sum = username.chars().sum();
        var expected = (sum % 1000) / 100000.0;

        var result = calculator.calculateSpreadFactor();

        Assertions.assertEquals(expected, result);
    }
}
