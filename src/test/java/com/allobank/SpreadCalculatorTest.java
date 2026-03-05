package com.allobank;

import org.junit.jupiter.api.Test;

import com.allobank.services.SpreadCalculator;

import static org.junit.jupiter.api.Assertions.*;

class SpreadCalculatorTest {

    @Test
    void calculate_withTestUsername_returnsExpected() {
        // Buat instance manual untuk test
        SpreadCalculator calc = new SpreadCalculator();
        // Set username via reflection atau buat method helper
        double result = calc.calculateFromUsername("alice"); // method helper untuk test
        assertEquals(0.00510, result, 0.00001);
    }
}
