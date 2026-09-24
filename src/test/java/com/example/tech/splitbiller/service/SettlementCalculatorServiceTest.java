package com.example.tech.splitbiller.service;

import com.example.tech.splitbiller.model.response.DetailSettlementResponse;
import com.example.tech.splitbiller.model.response.PersonBalanceResponse;
import com.example.tech.splitbiller.model.response.SettlementResponse;
import com.example.tech.splitbiller.util.GreedySettlementOptimizer;
import com.example.tech.splitbiller.util.ServiceChargeCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettlementCalculatorServiceTest {

    @Mock
    private ExpenseService expenseService;

    @Mock
    private BalanceCalculatorService balanceCalculatorService;

    @Mock
    private GreedySettlementOptimizer optimizer;

    @Mock
    private ServiceChargeCalculator serviceChargeCalculator;

    @InjectMocks
    private SettlementCalculatorService settlementCalculatorService;

    @Test
    @DisplayName("Should successfully coordinate balance calculation, settlement optimization, and service charges")
    void shouldCalculateSettlementsAndServiceChargesForGroup() {
        String groupId = "group-123";

        List<PersonBalanceResponse> mockBalances = List.of(
                new PersonBalanceResponse("1", "Alice", new BigDecimal("-50.00")),
                new PersonBalanceResponse("2", "Bob", new BigDecimal("50.00"))
        );

        List<SettlementResponse> expectedSettlements = List.of(
                new SettlementResponse("1", "Alice", "2", "Bob", new BigDecimal("50.00"))
        );

        BigDecimal totalExpenses = new BigDecimal("200.00");
        BigDecimal serviceChargePct = new BigDecimal("5.00");
        BigDecimal serviceChargeAmount = new BigDecimal("10.00");

        when(balanceCalculatorService.calculate(groupId)).thenReturn(mockBalances);
        when(optimizer.optimize(mockBalances)).thenReturn(expectedSettlements);
        when(expenseService.calculateTotalExpenses(groupId)).thenReturn(totalExpenses);
        when(serviceChargeCalculator.calculatePercentage()).thenReturn(serviceChargePct);
        when(serviceChargeCalculator.calculateAmount(totalExpenses)).thenReturn(serviceChargeAmount);

        DetailSettlementResponse response = settlementCalculatorService.calculate(groupId);

        assertNotNull(response);
        assertEquals(1, response.settlements().size());
        assertEquals("Alice", response.settlements().get(0).fromPersonName());
        assertEquals("Bob", response.settlements().get(0).toPersonName());
        assertEquals(0, new BigDecimal("50.00").compareTo(response.settlements().get(0).amount()));
        assertEquals(serviceChargePct, response.serviceChargePct());
        assertEquals(serviceChargeAmount, response.serviceChargeAmount());

        verify(balanceCalculatorService).calculate(groupId);
        verify(optimizer).optimize(mockBalances);
        verify(expenseService).calculateTotalExpenses(groupId);
        verify(serviceChargeCalculator).calculatePercentage();
        verify(serviceChargeCalculator).calculateAmount(totalExpenses);
    }
}
