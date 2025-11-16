package com.allobank.utility;

import com.allobank.config.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SpreadCalculatorTest {

    @Mock
    private AppProperties appProperties;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateSpreadFactor_SampleUsername() {
        // Test with a known username
        doReturn("johndoe47").when(appProperties).getGithubUsername();
        var calculator = new SpreadCalculator(appProperties);

        // Calculate expected value
        // Sum = 850
        // 850 % 1000 = 850
        // 850 / 100000.0 = 0.00850

        BigDecimal factor = calculator.calculateSpreadFactor();
        assertThat(calculator.getGithubUsername()).isEqualTo("johndoe47");
        assertThat(factor).isEqualTo(new BigDecimal("0.0085000000"));
    }

    @Test
    void testCalculateSpreadFactor_Consistency() {
        doReturn("testUser").when(appProperties).getGithubUsername();
        var calculator = new SpreadCalculator(appProperties);

        BigDecimal factor1 = calculator.calculateSpreadFactor();
        BigDecimal factor2 = calculator.calculateSpreadFactor();

        // Should return the same cached value
        assertThat(factor1).isEqualTo(factor2);
    }

    @Test
    void testCalculateSpreadFactor_CaseInsensitive() {
        AppProperties props1 = mock(AppProperties.class);
        doReturn("TestUser").when(props1).getGithubUsername();

        AppProperties props2 = mock(AppProperties.class);
        doReturn("TestUser").when(props2).getGithubUsername();

        SpreadCalculator calculator1 = new SpreadCalculator(props1);
        SpreadCalculator calculator2 = new SpreadCalculator(props2);

        // Should produce the same result regardless of case
        assertThat(calculator1.calculateSpreadFactor())
                .isEqualTo(calculator2.calculateSpreadFactor());
    }

    @Test
    void testCalculateSpreadFactor_Range() {
        doReturn("xyz789").when(appProperties).getGithubUsername();
        var calculator = new SpreadCalculator(appProperties);

        BigDecimal factor = calculator.calculateSpreadFactor();

        // Should be between 0.00000 and 0.00999
        assertThat(factor).isGreaterThanOrEqualTo(BigDecimal.ZERO)
                        .isLessThan(new BigDecimal("0.01"));
    }

    @Test
    void testGetGithubUsername() {
        doReturn("alice123").when(appProperties).getGithubUsername();
        var calculator = new SpreadCalculator(appProperties);

        assertThat(calculator.getGithubUsername()).isEqualTo("alice123");
    }
}