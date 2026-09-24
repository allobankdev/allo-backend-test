package com.example.tech.splitbiller.util;

import com.example.tech.splitbiller.model.response.PersonBalanceResponse;
import com.example.tech.splitbiller.model.response.SettlementResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GreedySettlementOptimizerTest {

    private GreedySettlementOptimizer optimizer;

    @BeforeEach
    void setUp() {
        optimizer = new GreedySettlementOptimizer();
    }

    @Test
    @DisplayName("Should return empty list when balances list is empty")
    void shouldReturnEmptyListWhenBalancesAreEmpty() {
        List<SettlementResponse> result = optimizer.optimize(Collections.emptyList());

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when all balances are zero")
    void shouldReturnEmptyListWhenAllBalancesAreZero() {
        List<PersonBalanceResponse> balances = List.of(
                new PersonBalanceResponse("1", "Alice", BigDecimal.ZERO),
                new PersonBalanceResponse("2", "Bob", BigDecimal.ZERO)
        );

        List<SettlementResponse> result = optimizer.optimize(balances);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should settle two people with exact matching balance")
    void shouldSettleTwoPeopleWithMatchingBalance() {
        List<PersonBalanceResponse> balances = List.of(
                new PersonBalanceResponse("1", "Alice", new BigDecimal("-100.00")),
                new PersonBalanceResponse("2", "Bob", new BigDecimal("100.00"))
        );

        List<SettlementResponse> result = optimizer.optimize(balances);

        assertEquals(1, result.size());
        SettlementResponse settlement = result.get(0);
        assertEquals("1", settlement.fromPersonId());
        assertEquals("Alice", settlement.fromPersonName());
        assertEquals("2", settlement.toPersonId());
        assertEquals("Bob", settlement.toPersonName());
        assertEquals(0, new BigDecimal("100.00").compareTo(settlement.amount()));
    }

    @Test
    @DisplayName("Should settle single debtor across multiple creditors")
    void shouldSettleSingleDebtorWithMultipleCreditors() {
        List<PersonBalanceResponse> balances = List.of(
                new PersonBalanceResponse("1", "Alice", new BigDecimal("-100.00")),
                new PersonBalanceResponse("2", "Bob", new BigDecimal("60.00")),
                new PersonBalanceResponse("3", "Charlie", new BigDecimal("40.00"))
        );

        List<SettlementResponse> result = optimizer.optimize(balances);

        assertEquals(2, result.size());

        // First settlement: Alice pays Bob 60.00
        SettlementResponse first = result.get(0);
        assertEquals("1", first.fromPersonId());
        assertEquals("Alice", first.fromPersonName());
        assertEquals("2", first.toPersonId());
        assertEquals("Bob", first.toPersonName());
        assertEquals(0, new BigDecimal("60.00").compareTo(first.amount()));

        // Second settlement: Alice pays Charlie 40.00
        SettlementResponse second = result.get(1);
        assertEquals("1", second.fromPersonId());
        assertEquals("Alice", second.fromPersonName());
        assertEquals("3", second.toPersonId());
        assertEquals("Charlie", second.toPersonName());
        assertEquals(0, new BigDecimal("40.00").compareTo(second.amount()));
    }

    @Test
    @DisplayName("Should settle multiple debtors with a single creditor")
    void shouldSettleMultipleDebtorsWithSingleCreditor() {
        List<PersonBalanceResponse> balances = List.of(
                new PersonBalanceResponse("1", "Alice", new BigDecimal("-30.00")),
                new PersonBalanceResponse("2", "Bob", new BigDecimal("-70.00")),
                new PersonBalanceResponse("3", "Charlie", new BigDecimal("100.00"))
        );

        List<SettlementResponse> result = optimizer.optimize(balances);

        assertEquals(2, result.size());

        // First settlement: Alice pays Charlie 30.00
        SettlementResponse first = result.get(0);
        assertEquals("1", first.fromPersonId());
        assertEquals("Alice", first.fromPersonName());
        assertEquals("3", first.toPersonId());
        assertEquals("Charlie", first.toPersonName());
        assertEquals(0, new BigDecimal("30.00").compareTo(first.amount()));

        // Second settlement: Bob pays Charlie 70.00
        SettlementResponse second = result.get(1);
        assertEquals("2", second.fromPersonId());
        assertEquals("Bob", second.fromPersonName());
        assertEquals("3", second.toPersonId());
        assertEquals("Charlie", second.toPersonName());
        assertEquals(0, new BigDecimal("70.00").compareTo(second.amount()));
    }

    @Test
    @DisplayName("Should handle complex multi-debtor and multi-creditor settlement")
    void shouldHandleComplexMultiPersonSettlement() {
        List<PersonBalanceResponse> balances = List.of(
                new PersonBalanceResponse("1", "Alice", new BigDecimal("-50.00")),
                new PersonBalanceResponse("2", "Bob", new BigDecimal("-25.00")),
                new PersonBalanceResponse("3", "Charlie", new BigDecimal("35.00")),
                new PersonBalanceResponse("4", "David", new BigDecimal("40.00"))
        );

        List<SettlementResponse> result = optimizer.optimize(balances);

        assertEquals(3, result.size());

        // 1. Alice pays Charlie 35.00 (Charlie cleared, Alice remaining debt: 15.00)
        SettlementResponse s1 = result.get(0);
        assertEquals("1", s1.fromPersonId());
        assertEquals("Alice", s1.fromPersonName());
        assertEquals("3", s1.toPersonId());
        assertEquals("Charlie", s1.toPersonName());
        assertEquals(0, new BigDecimal("35.00").compareTo(s1.amount()));

        // 2. Alice pays David 15.00 (Alice cleared, David remaining credit: 25.00)
        SettlementResponse s2 = result.get(1);
        assertEquals("1", s2.fromPersonId());
        assertEquals("Alice", s2.fromPersonName());
        assertEquals("4", s2.toPersonId());
        assertEquals("David", s2.toPersonName());
        assertEquals(0, new BigDecimal("15.00").compareTo(s2.amount()));

        // 3. Bob pays David 25.00 (Bob cleared, David cleared)
        SettlementResponse s3 = result.get(2);
        assertEquals("2", s3.fromPersonId());
        assertEquals("Bob", s3.fromPersonName());
        assertEquals("4", s3.toPersonId());
        assertEquals("David", s3.toPersonName());
        assertEquals(0, new BigDecimal("25.00").compareTo(s3.amount()));
    }

    @Test
    @DisplayName("Should handle decimal amounts correctly")
    void shouldHandleDecimalAmountsCorrectly() {
        List<PersonBalanceResponse> balances = List.of(
                new PersonBalanceResponse("1", "Alice", new BigDecimal("-33.33")),
                new PersonBalanceResponse("2", "Bob", new BigDecimal("-66.67")),
                new PersonBalanceResponse("3", "Charlie", new BigDecimal("100.00"))
        );

        List<SettlementResponse> result = optimizer.optimize(balances);

        assertEquals(2, result.size());

        assertEquals(0, new BigDecimal("33.33").compareTo(result.get(0).amount()));
        assertEquals(0, new BigDecimal("66.67").compareTo(result.get(1).amount()));
    }

    @Test
    @DisplayName("Should return empty list when only debtors or only creditors are present")
    void shouldReturnEmptyListWhenOnlyDebtorsOrOnlyCreditors() {
        List<PersonBalanceResponse> onlyDebtors = List.of(
                new PersonBalanceResponse("1", "Alice", new BigDecimal("-50.00"))
        );
        assertTrue(optimizer.optimize(onlyDebtors).isEmpty());

        List<PersonBalanceResponse> onlyCreditors = List.of(
                new PersonBalanceResponse("2", "Bob", new BigDecimal("50.00"))
        );
        assertTrue(optimizer.optimize(onlyCreditors).isEmpty());
    }
}
