package com.allobank.splitbill.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SettlementCalculatorTest {
    @Test
    void netsMultipleExpensesAndProducesOnlyOutstandingTransfers() {
        UUID alice = UUID.randomUUID();
        UUID bob = UUID.randomUUID();
        UUID charlie = UUID.randomUUID();
        SettlementCalculator calculator = new SettlementCalculator();

        SettlementCalculator.Result result = calculator.calculate(List.of(alice, bob, charlie), List.of(
                new SettlementCalculator.Expense(alice, new BigDecimal("90.00"), Map.of(
                        alice, new BigDecimal("30.00"), bob, new BigDecimal("30.00"),
                        charlie, new BigDecimal("30.00"))),
                new SettlementCalculator.Expense(bob, new BigDecimal("30.00"), Map.of(
                        alice, new BigDecimal("10.00"), bob, new BigDecimal("10.00"),
                        charlie, new BigDecimal("10.00")))));

        assertEquals(new BigDecimal("50.00"), result.balances().get(alice));
        assertEquals(new BigDecimal("-10.00"), result.balances().get(bob));
        assertEquals(new BigDecimal("-40.00"), result.balances().get(charlie));
        assertEquals(2, result.transfers().size());
        assertEquals(bob, result.transfers().get(0).from());
        assertEquals(alice, result.transfers().get(0).to());
        assertEquals(new BigDecimal("10.00"), result.transfers().get(0).amount());
        assertEquals(charlie, result.transfers().get(1).from());
        assertEquals(new BigDecimal("40.00"), result.transfers().get(1).amount());
    }

    @Test
    void recordedPaymentReducesBothPartiesOutstandingBalance() {
        UUID alice = UUID.randomUUID();
        UUID bob = UUID.randomUUID();
        SettlementCalculator calculator = new SettlementCalculator();

        SettlementCalculator.Result result = calculator.calculate(List.of(alice, bob), List.of(
                new SettlementCalculator.Expense(alice, new BigDecimal("100.00"), Map.of(
                        alice, new BigDecimal("50.00"), bob, new BigDecimal("50.00")))), List.of(
                new SettlementCalculator.Payment(bob, alice, new BigDecimal("20.00"))));

        assertEquals(new BigDecimal("30.00"), result.balances().get(alice));
        assertEquals(new BigDecimal("-30.00"), result.balances().get(bob));
        assertEquals(new BigDecimal("30.00"), result.transfers().getFirst().amount());
    }

    @Test
    void swappedPaymentPartiesActAsAnAppendOnlyReversal() {
        UUID alice = UUID.randomUUID();
        UUID bob = UUID.randomUUID();
        SettlementCalculator calculator = new SettlementCalculator();

        SettlementCalculator.Result result = calculator.calculate(List.of(alice, bob), List.of(
                new SettlementCalculator.Expense(alice, new BigDecimal("100.00"), Map.of(
                        alice, new BigDecimal("50.00"), bob, new BigDecimal("50.00")))), List.of(
                new SettlementCalculator.Payment(bob, alice, new BigDecimal("20.00")),
                new SettlementCalculator.Payment(alice, bob, new BigDecimal("20.00"))));

        assertEquals(new BigDecimal("50.00"), result.balances().get(alice));
        assertEquals(new BigDecimal("-50.00"), result.balances().get(bob));
    }
}
