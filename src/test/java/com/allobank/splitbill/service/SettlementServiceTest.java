package com.allobank.splitbill.service;

import com.allobank.splitbill.domain.entity.BillGroup;
import com.allobank.splitbill.domain.entity.Expense;
import com.allobank.splitbill.domain.entity.ExpenseSplit;
import com.allobank.splitbill.domain.entity.Participant;
import com.allobank.splitbill.domain.enums.SplitType;
import com.allobank.splitbill.dto.response.SettlementSummaryResponse;
import com.allobank.splitbill.dto.response.SettlementTransactionResponse;
import com.allobank.splitbill.repository.BillGroupRepository;
import com.allobank.splitbill.repository.ExpenseRepository;
import com.allobank.splitbill.repository.PaymentRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock
    private GroupService groupService;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private PaymentRecordRepository paymentRecordRepository;

    @Mock
    private PersonalizationService personalizationService;

    @InjectMocks
    private SettlementService settlementService;

    private BillGroup sampleGroup;
    private Participant alice;
    private Participant bob;
    private Participant charlie;

    @BeforeEach
    void setUp() {
        alice = Participant.builder().id(1L).name("Alice").build();
        bob = Participant.builder().id(2L).name("Bob").build();
        charlie = Participant.builder().id(3L).name("Charlie").build();

        sampleGroup = BillGroup.builder()
                .id(100L)
                .name("Weekend Trip")
                .participants(List.of(alice, bob, charlie))
                .build();
    }

    @Test
    @DisplayName("Should correctly calculate settlement summary and debt simplification")
    void testSettlementCalculationAndDebtSimplification() {
        // Scenario:
        // Expense 1: Alice paid 300.00 split equally among Alice, Bob, Charlie (100.00 each)
        // Expense 2: Bob paid 150.00 split equally among Alice, Bob, Charlie (50.00 each)
        // Total expenses = 450.00
        // Net Balances:
        // Alice: Paid 300 - Owed 150 = +150 (Creditor)
        // Bob: Paid 150 - Owed 150 = 0
        // Charlie: Paid 0 - Owed 150 = -150 (Debtor)
        // Expected Simplified Settlement: Charlie owes Alice 150.00

        Expense expense1 = Expense.builder()
                .id(1L)
                .description("Lunch")
                .totalAmount(new BigDecimal("300.00"))
                .paidBy(alice)
                .splitType(SplitType.EQUAL)
                .splits(List.of(
                        ExpenseSplit.builder().id(1L).participant(alice).shareAmount(new BigDecimal("100.00")).build(),
                        ExpenseSplit.builder().id(2L).participant(bob).shareAmount(new BigDecimal("100.00")).build(),
                        ExpenseSplit.builder().id(3L).participant(charlie).shareAmount(new BigDecimal("100.00")).build()
                ))
                .build();

        Expense expense2 = Expense.builder()
                .id(2L)
                .description("Fuel")
                .totalAmount(new BigDecimal("150.00"))
                .paidBy(bob)
                .splitType(SplitType.EQUAL)
                .splits(List.of(
                        ExpenseSplit.builder().id(4L).participant(alice).shareAmount(new BigDecimal("50.00")).build(),
                        ExpenseSplit.builder().id(5L).participant(bob).shareAmount(new BigDecimal("50.00")).build(),
                        ExpenseSplit.builder().id(6L).participant(charlie).shareAmount(new BigDecimal("50.00")).build()
                ))
                .build();

        when(groupService.getGroupEntity(100L)).thenReturn(sampleGroup);
        when(expenseRepository.findByGroupId(100L)).thenReturn(List.of(expense1, expense2));
        when(paymentRecordRepository.findByGroupId(100L)).thenReturn(Collections.emptyList());
        when(personalizationService.calculateServiceChargePct()).thenReturn(5);
        when(personalizationService.calculateServiceChargeAmount(new BigDecimal("450.00"))).thenReturn(new BigDecimal("22.50"));

        SettlementSummaryResponse response = settlementService.getSettlementSummary(100L);

        assertNotNull(response);
        assertEquals(100L, response.getGroupId());
        assertEquals("Weekend Trip", response.getGroupName());
        assertEquals(new BigDecimal("450.00"), response.getTotalGroupExpenses());

        // Personalization checks
        assertEquals(5, response.getServiceChargePct());
        assertEquals(new BigDecimal("22.50"), response.getServiceChargeAmount());

        // Settlements check
        List<SettlementTransactionResponse> settlements = response.getSettlements();
        assertEquals(1, settlements.size(), "Should simplify debts into exactly 1 transaction");
        SettlementTransactionResponse tx = settlements.get(0);
        assertEquals("Charlie", tx.getFrom().getName());
        assertEquals("Alice", tx.getTo().getName());
        assertEquals(new BigDecimal("150.00"), tx.getAmount());
    }
}
