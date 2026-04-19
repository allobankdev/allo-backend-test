package com.allobank.allobackend;

import com.allobank.allobackend.common.util.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilsTest {

    @Test
    @DisplayName("Check buy spread must (1/rate * (1+factor))")
    void testCalculateBuySpread(){

        double rate = 0.0000625;
        double factor = 0.05;

        double result = Utils.calculateBuySpread(rate, factor);
        assertEquals(16800.0, result, 0.0001, "Kalkulasi Buy Spread not same");

    }

    @ParameterizedTest
    @CsvSource({
            "ashriprastiko, 0.0412",
            "allo, 0.0424",
            "'', 0.0"
    })
    @DisplayName("Must result factor based length username")
    void testCalculateSpreadFactorByUsername(String username, double expectedFactor) {
        double actualFactor = Utils.caculateSpreadFactorByUsername(username);
        assertEquals(expectedFactor, actualFactor, 0.0001);
    }

    @Test
    @DisplayName("Must handle devide with 0 in rate")
    void testCalculateBuySpread_ZeroRate() {
        double result = Utils.calculateBuySpread(0.0, 0.05);
        assertTrue(Double.isInfinite(result) || result == 0, "Must handle rate 0");
    }

}
