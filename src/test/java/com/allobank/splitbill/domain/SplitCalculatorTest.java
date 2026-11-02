package com.allobank.splitbill.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SplitCalculatorTest {
    private final SplitCalculator calculator = new SplitCalculator();
    private final UUID alice = UUID.randomUUID();
    private final UUID bob = UUID.randomUUID();
    private final UUID charlie = UUID.randomUUID();
    private final List<UUID> participants = List.of(alice, bob, charlie);

    @Test
    void equalSplitAssignsRemainderCentsInParticipantOrder() {
        Map<UUID, BigDecimal> result = calculator.allocate(new BigDecimal("100.00"),
                SplitType.EQUAL, participants, List.of());

        assertEquals(new BigDecimal("33.34"), result.get(alice));
        assertEquals(new BigDecimal("33.33"), result.get(bob));
        assertEquals(new BigDecimal("33.33"), result.get(charlie));
    }

    @Test
    void percentageSplitUsesLargestRemainder() {
        Map<UUID, BigDecimal> result = calculator.allocate(new BigDecimal("0.05"),
                SplitType.PERCENTAGE, participants, List.of(
                        new SplitShare(alice, null, new BigDecimal("40")),
                        new SplitShare(bob, null, new BigDecimal("35")),
                        new SplitShare(charlie, null, new BigDecimal("25"))));

        assertEquals(new BigDecimal("0.02"), result.get(alice));
        assertEquals(new BigDecimal("0.02"), result.get(bob));
        assertEquals(new BigDecimal("0.01"), result.get(charlie));
    }

    @Test
    void exactSplitRejectsMismatchedTotal() {
        assertThrows(IllegalArgumentException.class, () -> calculator.allocate(new BigDecimal("10.00"),
                SplitType.EXACT, participants, List.of(
                        new SplitShare(alice, new BigDecimal("4.00"), null),
                        new SplitShare(bob, new BigDecimal("5.00"), null))));
    }
}
