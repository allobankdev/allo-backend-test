package com.allobank.splitbill.service;

import com.allobank.splitbill.domain.ExpenseCalculator.ShareAllocation;
import com.allobank.splitbill.domain.SettlementCalculator;
import com.allobank.splitbill.domain.Transfer;
import com.allobank.splitbill.persistence.BillRepository;
import com.allobank.splitbill.persistence.BillRepository.ExpenseRow;
import com.allobank.splitbill.persistence.BillRepository.GroupRow;
import com.allobank.splitbill.persistence.BillRepository.ParticipantRow;
import com.allobank.splitbill.persistence.BillRepository.PaymentRow;
import com.allobank.splitbill.persistence.BillRepository.ShareRow;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SettlementService {
    private static final BigDecimal ZERO = new BigDecimal("0.00");

    private final BillRepository repository;
    private final SettlementCalculator calculator = new SettlementCalculator();

    public SettlementService(BillRepository repository) {
        this.repository = repository;
    }

    public GroupSettlement calculate(UUID groupId) {
        GroupRow group = repository.findGroup(groupId)
                .orElseThrow(() -> new NotFoundException("group not found"));
        List<ParticipantRow> participants = repository.findParticipants(groupId);
        List<ExpenseRow> expenses = repository.findExpenses(groupId);
        Map<UUID, Map<UUID, ShareAllocation>> sharesByExpense = sharesByExpense(groupId);

        List<SettlementCalculator.Expense> calculationExpenses = expenses.stream()
                .map(expense -> new SettlementCalculator.Expense(expense.paidBy(), expense.amount(),
                        sharesByExpense.getOrDefault(expense.id(), Map.of()).entrySet().stream()
                                .collect(LinkedHashMap::new,
                                        (map, entry) -> map.put(entry.getKey(), entry.getValue().amount()),
                                        LinkedHashMap::putAll)))
                .toList();
        List<PaymentRow> payments = repository.findPayments(groupId);
        List<SettlementCalculator.Payment> calculationPayments = payments.stream()
                .map(payment -> new SettlementCalculator.Payment(
                        payment.payerId(), payment.receiverId(), payment.amount()))
                .toList();
        SettlementCalculator.Result result = calculator.calculate(
                participants.stream().map(ParticipantRow::id).toList(),
                calculationExpenses, calculationPayments);

        BigDecimal totalExpenses = expenses.stream().map(ExpenseRow::amount)
                .reduce(ZERO, BigDecimal::add);
        BigDecimal totalSubtotal = expenses.stream().map(ExpenseRow::subtotal)
                .reduce(ZERO, BigDecimal::add);
        BigDecimal totalTax = expenses.stream().map(ExpenseRow::taxAmount)
                .reduce(ZERO, BigDecimal::add);
        return new GroupSettlement(group, participants, expenses, payments, totalSubtotal, totalTax,
                totalExpenses, result.balances(), result.transfers());
    }

    public Map<UUID, Map<UUID, ShareAllocation>> sharesByExpense(UUID groupId) {
        Map<UUID, Map<UUID, ShareAllocation>> result = new HashMap<>();
        for (ShareRow share : repository.findShares(groupId)) {
            result.computeIfAbsent(share.expenseId(), ignored -> new LinkedHashMap<>())
                    .put(share.participantId(), new ShareAllocation(
                            share.subtotal(), share.taxAmount(), share.amount()));
        }
        return result;
    }

    public record GroupSettlement(
            GroupRow group,
            List<ParticipantRow> participants,
            List<ExpenseRow> expenses,
            List<PaymentRow> payments,
            BigDecimal totalSubtotal,
            BigDecimal totalTax,
            BigDecimal totalExpenses,
            Map<UUID, BigDecimal> balances,
            List<Transfer> transfers
    ) {}
}
