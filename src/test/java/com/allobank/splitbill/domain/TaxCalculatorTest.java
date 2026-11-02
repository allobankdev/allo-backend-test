package com.allobank.splitbill.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaxCalculatorTest {
    private final TaxCalculator calculator = new TaxCalculator();

    @Test
    void exclusiveTaxTreatsInputAsPreTaxSubtotal() {
        TaxCalculator.Result result = calculator.calculate(
                new BigDecimal("100000.00"), TaxMode.EXCLUSIVE, new BigDecimal("11.00"));

        assertEquals(new BigDecimal("100000.00"), result.subtotal());
        assertEquals(new BigDecimal("11000.00"), result.taxAmount());
        assertEquals(new BigDecimal("111000.00"), result.totalAmount());
    }

    @Test
    void inclusiveTaxExtractsTaxWithoutChangingInputTotal() {
        TaxCalculator.Result result = calculator.calculate(
                new BigDecimal("111000.00"), TaxMode.INCLUSIVE, new BigDecimal("11.00"));

        assertEquals(new BigDecimal("100000.00"), result.subtotal());
        assertEquals(new BigDecimal("11000.00"), result.taxAmount());
        assertEquals(new BigDecimal("111000.00"), result.totalAmount());
    }

    @Test
    void noneRejectsNonZeroTaxRate() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(
                new BigDecimal("100.00"), TaxMode.NONE, new BigDecimal("11.00")));
    }
}
