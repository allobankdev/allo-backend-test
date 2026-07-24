package com.allobankdev.splitbill.service;

import com.allobankdev.splitbill.dto.expense.ExpenseRequestDTO;
import com.allobankdev.splitbill.dto.expense.ExpenseResponseDTO;
import com.allobankdev.splitbill.dto.settlement.SettlementResponseDTO;
import com.allobankdev.splitbill.dto.settlement.TransactionDTO;
import com.allobankdev.splitbill.entity.BillGroup;
import com.allobankdev.splitbill.entity.Expense;
import com.allobankdev.splitbill.mapper.EntityMapper;
import com.allobankdev.splitbill.repository.ExpenseRepository;
import com.allobankdev.splitbill.settlement.SettlementOptimizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final BillGroupService billGroupService;
    private final EntityMapper entityMapper;
    private final PersonalizationService personalizationService;
    private final SettlementOptimizationService settlementOptimizationService;

    // Hardcoded per requirements
    private static final String GITHUB_USERNAME = "NekoSukuriputo";

    @Transactional
    public ExpenseResponseDTO addExpense(String groupId, ExpenseRequestDTO request) {
        BillGroup group = billGroupService.getGroupEntityById(groupId);

        // Validation: verify paidBy is a participant
        if (!group.getParticipants().contains(request.getPaidBy())) {
            throw new IllegalArgumentException("User " + request.getPaidBy() + " is not a participant in this group.");
        }

        // Validation: verify splitAmong are participants
        List<String> splitAmong = request.getSplitAmong();
        if (splitAmong == null || splitAmong.isEmpty()) {
            splitAmong = group.getParticipants();
        } else {
            for (String p : splitAmong) {
                if (!group.getParticipants().contains(p)) {
                    throw new IllegalArgumentException("User " + p + " is not a participant in this group.");
                }
            }
        }

        Expense expense = Expense.builder()
                .billGroup(group)
                .description(request.getDescription())
                .paidBy(request.getPaidBy())
                .amount(request.getAmount())
                .splitAmong(splitAmong)
                .build();

        Expense savedExpense = expenseRepository.save(expense);
        return entityMapper.toExpenseResponseDTO(savedExpense);
    }

    @Transactional(readOnly = true)
    public SettlementResponseDTO getSettlementSummary(String groupId) {
        BillGroup group = billGroupService.getGroupEntityById(groupId);
        List<Expense> expenses = expenseRepository.findByBillGroupId(groupId);

        BigDecimal totalExpenses = BigDecimal.ZERO;
        Map<String, BigDecimal> balances = new HashMap<>();

        // Initialize balances for all participants
        for (String participant : group.getParticipants()) {
            balances.put(participant, BigDecimal.ZERO);
        }

        for (Expense expense : expenses) {
            totalExpenses = totalExpenses.add(expense.getAmount());

            // The person who paid gets a positive balance for the amount
            balances.put(expense.getPaidBy(), balances.get(expense.getPaidBy()).add(expense.getAmount()));

            // Split the amount
            int numSplitters = expense.getSplitAmong().size();
            BigDecimal splitAmount = expense.getAmount().divide(new BigDecimal(numSplitters), 4, RoundingMode.HALF_UP);

            // Deduct split amount from each person's balance (they owe this amount)
            for (String splitter : expense.getSplitAmong()) {
                balances.put(splitter, balances.get(splitter).subtract(splitAmount));
            }
        }

        // Calculate personalization
        int serviceChargePct = personalizationService.calculateServiceChargePct(GITHUB_USERNAME);
        BigDecimal pctDecimal = new BigDecimal(serviceChargePct).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal serviceChargeAmount = totalExpenses.multiply(pctDecimal).setScale(2, RoundingMode.HALF_UP);

        // Optimize settlements
        List<TransactionDTO> transactions = settlementOptimizationService.optimizeSettlements(balances);

        return SettlementResponseDTO.builder()
                .groupId(groupId)
                .totalExpenses(totalExpenses.setScale(2, RoundingMode.HALF_UP))
                .serviceChargePct(serviceChargePct)
                .serviceChargeAmount(serviceChargeAmount)
                .transactions(transactions)
                .build();
    }
}
