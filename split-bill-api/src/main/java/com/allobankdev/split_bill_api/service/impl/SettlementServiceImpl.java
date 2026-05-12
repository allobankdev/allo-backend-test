package com.allobankdev.split_bill_api.service.impl;

import com.allobankdev.split_bill_api.dto.SettlementResponse;
import com.allobankdev.split_bill_api.dto.SettlementTransactionResponse;
import com.allobankdev.split_bill_api.entity.BillGroup;
import com.allobankdev.split_bill_api.entity.Expense;
import com.allobankdev.split_bill_api.repository.ExpenseRepository;
import com.allobankdev.split_bill_api.repository.GroupRepository;
import com.allobankdev.split_bill_api.service.SettlementService;
import com.allobankdev.split_bill_api.settlement.SettlementCalculator;
import com.allobankdev.split_bill_api.settlement.TransactionResult;
import com.allobankdev.split_bill_api.util.ServiceChargeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl
        implements SettlementService {

    private final GroupRepository groupRepository;

    private final ExpenseRepository expenseRepository;

    private final SettlementCalculator
            settlementCalculator;

    @Override
    public SettlementResponse getSettlement(
            Long groupId) {

        BillGroup group =
                groupRepository.findById(groupId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Group not found"
                                ));

        List<Expense> expenses =
                expenseRepository.findByGroupId(
                        groupId);

        BigDecimal totalExpense =
                expenses.stream()
                        .map(Expense::getAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        List<TransactionResult> results =
                settlementCalculator.calculate(
                        expenses);

        List<SettlementTransactionResponse>
                transactions =
                results.stream()
                        .map(result ->
                                new SettlementTransactionResponse(
                                        result.getFrom(),
                                        result.getTo(),
                                        result.getAmount()
                                ))
                        .toList();

        return SettlementResponse.builder()
                .groupId(group.getId())
                .groupName(group.getName())
                .totalExpense(totalExpense)
                .serviceChargePct(
                        ServiceChargeUtil
                                .calculatePercentage()
                )
                .serviceChargeAmount(
                        ServiceChargeUtil
                                .calculateAmount(
                                        totalExpense)
                )
                .transactions(transactions)
                .build();
    }
}
