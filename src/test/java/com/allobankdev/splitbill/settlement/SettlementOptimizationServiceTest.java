package com.allobankdev.splitbill.settlement;

import com.allobankdev.splitbill.dto.settlement.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SettlementOptimizationServiceTest {

    private SettlementOptimizationService service;

    @BeforeEach
    void setUp() {
        service = new SettlementOptimizationService();
    }

    @Test
    void testOptimizeSettlements_EqualSplit() {
        // A paid 300, B and C paid 0. Each should pay 100.
        // Balances: A: +200, B: -100, C: -100
        Map<String, BigDecimal> balances = new HashMap<>();
        balances.put("Andi", new BigDecimal("200.00"));
        balances.put("Budi", new BigDecimal("-100.00"));
        balances.put("Citra", new BigDecimal("-100.00"));

        List<TransactionDTO> transactions = service.optimizeSettlements(balances);

        assertEquals(2, transactions.size());
        
        // Check total transactions amount equals 200
        BigDecimal totalAmount = transactions.stream()
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        assertEquals(new BigDecimal("200.00"), totalAmount);
    }
}
