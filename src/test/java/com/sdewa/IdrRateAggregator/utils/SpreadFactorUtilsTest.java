package com.sdewa.IdrRateAggregator.utils;

import org.junit.jupiter.api.Test;

import com.sdewa.IdrRateAggregator.uitls.SpreadFactorUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class SpreadFactorUtilsTest {
      @Test
    void testUsdBuySpreadCalculation() {
        String username = "dewadev"; 

        double rateUsd = 0.000058999; 
        double expectedSpreadFactor = 736 % 1000 / 100000.0;
        double expectedUsdBuySpread = (1 / rateUsd) * (1 + expectedSpreadFactor);

        double actualUsdBuySpread = SpreadFactorUtils.calculateUsdBuySpread(rateUsd, username);

        assertThat(actualUsdBuySpread).isCloseTo(expectedUsdBuySpread, within(0.01));
    }
}
