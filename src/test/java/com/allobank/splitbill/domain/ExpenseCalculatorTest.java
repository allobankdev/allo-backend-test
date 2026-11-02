package com.allobank.splitbill.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpenseCalculatorTest {
    private final ExpenseCalculator calculator = new ExpenseCalculator();
    private final UUID alice = UUID.randomUUID();
    private final UUID bob = UUID.randomUUID();
    private final UUID charlie = UUID.randomUUID();
    private final List<UUID> participants = List.of(alice, bob, charlie);

    @Test
    void exclusiveTaxIsAllocatedWithoutLosingRemainderCents() {
        ExpenseCalculator.Result result = calculator.calculate(new BigDecimal("100.00"),
                TaxMode.EXCLUSIVE, new BigDecimal("11.00"), SplitType.EQUAL,
                participants, List.of());

        assertEquals(new BigDecimal("3.67"), result.shares().get(alice).taxAmount());
        assertEquals(new BigDecimal("3.67"), result.shares().get(bob).taxAmount());
        assertEquals(new BigDecimal("3.66"), result.shares().get(charlie).taxAmount());
        assertEquals(new BigDecimal("111.00"), result.shares().values().stream()
                .map(ExpenseCalculator.ShareAllocation::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @Test
    void exactSplitAmountsArePreTaxWhenTaxIsExclusive() {
        ExpenseCalculator.Result result = calculator.calculate(new BigDecimal("100.00"),
                TaxMode.EXCLUSIVE, new BigDecimal("11.00"), SplitType.EXACT,
                participants, List.of(
                        new SplitShare(alice, new BigDecimal("60.00"), null),
                        new SplitShare(bob, new BigDecimal("40.00"), null)));

        assertEquals(new BigDecimal("66.60"), result.shares().get(alice).amount());
        assertEquals(new BigDecimal("44.40"), result.shares().get(bob).amount());
    }

    @Test
    void inclusiveTaxShareDetailsReconcileToExpenseTotals() {
        ExpenseCalculator.Result result = calculator.calculate(new BigDecimal("111.00"),
                TaxMode.INCLUSIVE, new BigDecimal("11.00"), SplitType.PERCENTAGE,
                participants, List.of(
                        new SplitShare(alice, null, new BigDecimal("50.00")),
                        new SplitShare(bob, null, new BigDecimal("30.00")),
                        new SplitShare(charlie, null, new BigDecimal("20.00"))));

        assertEquals(result.totals().subtotal(), result.shares().values().stream()
                .map(ExpenseCalculator.ShareAllocation::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        assertEquals(result.totals().taxAmount(), result.shares().values().stream()
                .map(ExpenseCalculator.ShareAllocation::taxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        assertEquals(result.totals().totalAmount(), result.shares().values().stream()
                .map(ExpenseCalculator.ShareAllocation::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
