package com.allo.splitbill.service;

import com.allo.splitbill.model.BillGroup;
import com.allo.splitbill.model.Debt;
import com.allo.splitbill.model.Expense;
import com.allo.splitbill.model.SettlementResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SettlementServiceTest {

    private SettlementService settlementService;

    @BeforeEach
    void setUp() {
        ServiceChargeCalculator calculator = new ServiceChargeCalculator("testuser");
        settlementService = new SettlementService(calculator);
    }

    @Test
    void testServiceChargeCalculator() {
        assertEquals(5, ServiceChargeCalculator.computeServiceChargePct("testuser"));
        assertEquals(7, ServiceChargeCalculator.computeServiceChargePct("kydevx"));
    }

    @Test
    void testMinimizeTransactions() {
        Map<String, BigDecimal> balance = Map.of(
                "Alice", new BigDecimal("30.00"),
                "Bob", new BigDecimal("-10.00"),
                "Charlie", new BigDecimal("-20.00")
        );

        List<Debt> debts = SettlementService.minimizeTransactions(balance);

        assertEquals(2, debts.size());
        assertTrue(debts.stream().anyMatch(d ->
                d.getFrom().equals("Bob") && d.getTo().equals("Alice") &&
                d.getAmount().compareTo(new BigDecimal("10.00")) == 0));
        assertTrue(debts.stream().anyMatch(d ->
                d.getFrom().equals("Charlie") && d.getTo().equals("Alice") &&
                d.getAmount().compareTo(new BigDecimal("20.00")) == 0));
    }

    @Test
    void testSettlementWithEqualSplit() {
        ServiceChargeCalculator calc = new ServiceChargeCalculator("kydevx");
        SettlementService service = new SettlementService(calc);

        BillGroup group = new BillGroup("Lunch", List.of("Alice", "Bob", "Charlie"));

        group.getExpenses().add(new Expense("Alice", new BigDecimal("60.00"), "Pizza",
                List.of("Alice", "Bob", "Charlie")));

        SettlementResponse response = service.calculate(group);

        assertEquals(new BigDecimal("60.00"), response.getTotalExpenses());
        assertEquals(7, response.getServiceChargePct());

        BigDecimal expectedCharge = new BigDecimal("60.00")
                .multiply(BigDecimal.valueOf(7))
                .divide(BigDecimal.valueOf(100));
        assertEquals(expectedCharge, response.getServiceChargeAmount());

        assertEquals(2, response.getDebts().size());

        for (Debt d : response.getDebts()) {
            if (d.getTo().equals("Alice")) {
                assertTrue(d.getAmount().compareTo(BigDecimal.ZERO) > 0);
            }
        }
    }

    @Test
    void testNoDebtsWhenBalancesAreZero() {
        Map<String, BigDecimal> balance = Map.of(
                "Alice", BigDecimal.ZERO,
                "Bob", BigDecimal.ZERO
        );

        List<Debt> debts = SettlementService.minimizeTransactions(balance);
        assertTrue(debts.isEmpty());
    }

    @Test
    void testSingleDebtorSingleCreditor() {
        Map<String, BigDecimal> balance = Map.of(
                "Alice", new BigDecimal("100.00"),
                "Bob", new BigDecimal("-100.00")
        );

        List<Debt> debts = SettlementService.minimizeTransactions(balance);

        assertEquals(1, debts.size());
        assertEquals("Bob", debts.get(0).getFrom());
        assertEquals("Alice", debts.get(0).getTo());
        assertEquals(new BigDecimal("100.00"), debts.get(0).getAmount());
    }

    @Test
    void testServiceChargeAmount_scalesWithTotal() {
        ServiceChargeCalculator calc = new ServiceChargeCalculator("kydevx");
        assertEquals(new BigDecimal("7.00"), calc.calculateServiceChargeAmount(new BigDecimal("100.00")));
        assertEquals(new BigDecimal("0.70"), calc.calculateServiceChargeAmount(new BigDecimal("10.00")));
        assertEquals(BigDecimal.ZERO, calc.calculateServiceChargeAmount(BigDecimal.ZERO));
    }
}
