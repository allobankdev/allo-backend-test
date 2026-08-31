package com.allobank.splitbill.service;

import com.allobank.splitbill.domain.entity.BillGroup;
import com.allobank.splitbill.domain.entity.Expense;
import com.allobank.splitbill.domain.entity.ExpenseSplit;
import com.allobank.splitbill.domain.entity.Participant;
import com.allobank.splitbill.domain.entity.PaymentRecord;
import com.allobank.splitbill.dto.response.*;
import com.allobank.splitbill.repository.ExpenseRepository;
import com.allobank.splitbill.repository.PaymentRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final GroupService groupService;
    private final ExpenseRepository expenseRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final PersonalizationService personalizationService;

    @Transactional(readOnly = true)
    public SettlementSummaryResponse getSettlementSummary(Long groupId) {
        BillGroup group = groupService.getGroupEntity(groupId);
        List<Participant> participants = group.getParticipants();
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        List<PaymentRecord> payments = paymentRecordRepository.findByGroupId(groupId);

        // 1. Calculate total group expenses
        BigDecimal totalGroupExpenses = expenses.stream()
                .map(Expense::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // 2. Track paid and owed per participant ID
        Map<Long, BigDecimal> paidMap = new HashMap<>();
        Map<Long, BigDecimal> owedMap = new HashMap<>();
        Map<Long, BigDecimal> netMap = new HashMap<>();

        for (Participant p : participants) {
            paidMap.put(p.getId(), BigDecimal.ZERO);
            owedMap.put(p.getId(), BigDecimal.ZERO);
            netMap.put(p.getId(), BigDecimal.ZERO);
        }

        for (Expense expense : expenses) {
            Long paidById = expense.getPaidBy().getId();
            paidMap.put(paidById, paidMap.getOrDefault(paidById, BigDecimal.ZERO).add(expense.getTotalAmount()));

            for (ExpenseSplit split : expense.getSplits()) {
                Long partId = split.getParticipant().getId();
                owedMap.put(partId, owedMap.getOrDefault(partId, BigDecimal.ZERO).add(split.getShareAmount()));
            }
        }

        // Net balance from expenses: Paid - Owed
        for (Participant p : participants) {
            Long id = p.getId();
            BigDecimal paid = paidMap.getOrDefault(id, BigDecimal.ZERO);
            BigDecimal owed = owedMap.getOrDefault(id, BigDecimal.ZERO);
            netMap.put(id, paid.subtract(owed));
        }

        // Adjust net balance with recorded direct settlement payments
        // If Payer (from) pays Recipient (to) $X:
        // Payer sent money -> Payer's net balance increases by +$X (reduces their debt)
        // Recipient received money -> Recipient's net balance decreases by -$X (reduces their credit)
        for (PaymentRecord payment : payments) {
            Long fromId = payment.getFromParticipant().getId();
            Long toId = payment.getToParticipant().getId();
            BigDecimal amount = payment.getAmount();

            if (netMap.containsKey(fromId)) {
                netMap.put(fromId, netMap.get(fromId).add(amount));
            }
            if (netMap.containsKey(toId)) {
                netMap.put(toId, netMap.get(toId).subtract(amount));
            }
        }

        // Build participant balance response list
        List<ParticipantBalanceResponse> balanceResponses = new ArrayList<>();
        Map<Long, ParticipantResponse> participantDtoMap = new HashMap<>();

        for (Participant p : participants) {
            Long id = p.getId();
            ParticipantResponse pDto = ParticipantResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .build();
            participantDtoMap.put(id, pDto);

            BigDecimal paid = paidMap.getOrDefault(id, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
            BigDecimal owed = owedMap.getOrDefault(id, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
            BigDecimal net = netMap.getOrDefault(id, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);

            balanceResponses.add(ParticipantBalanceResponse.builder()
                    .participantId(id)
                    .participantName(p.getName())
                    .totalPaid(paid)
                    .totalOwed(owed)
                    .netBalance(net)
                    .build());
        }

        // 3. Optimized Settlement Algorithm (Minimizing Transactions)
        List<SettlementTransactionResponse> transactions = simplifyDebts(netMap, participantDtoMap);

        // 4. Personalization calculation
        int serviceChargePct = personalizationService.calculateServiceChargePct();
        BigDecimal serviceChargeAmount = personalizationService.calculateServiceChargeAmount(totalGroupExpenses);

        return SettlementSummaryResponse.builder()
                .groupId(group.getId())
                .groupName(group.getName())
                .totalGroupExpenses(totalGroupExpenses)
                .serviceChargePct(serviceChargePct)
                .serviceChargeAmount(serviceChargeAmount)
                .participantBalances(balanceResponses)
                .settlements(transactions)
                .build();
    }

    /**
     * Greedy Debt Simplification Algorithm
     * Minimizes transaction count by pairing largest debtors with largest creditors.
     */
    private List<SettlementTransactionResponse> simplifyDebts(
            Map<Long, BigDecimal> netMap,
            Map<Long, ParticipantResponse> participantDtoMap) {

        List<SettlementTransactionResponse> transactions = new ArrayList<>();

        // Debtors have netBalance < 0
        // Creditors have netBalance > 0
        class BalanceNode {
            final Long id;
            BigDecimal balance;
            BalanceNode(Long id, BigDecimal balance) {
                this.id = id;
                this.balance = balance;
            }
        }

        List<BalanceNode> debtors = new ArrayList<>();
        List<BalanceNode> creditors = new ArrayList<>();

        for (Map.Entry<Long, BigDecimal> entry : netMap.entrySet()) {
            BigDecimal val = entry.getValue().setScale(2, RoundingMode.HALF_UP);
            if (val.compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(new BalanceNode(entry.getKey(), val.abs()));
            } else if (val.compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(new BalanceNode(entry.getKey(), val));
            }
        }

        // Sort descending by amount
        debtors.sort((a, b) -> b.balance.compareTo(a.balance));
        creditors.sort((a, b) -> b.balance.compareTo(a.balance));

        int i = 0;
        int j = 0;

        while (i < debtors.size() && j < creditors.size()) {
            BalanceNode debtor = debtors.get(i);
            BalanceNode creditor = creditors.get(j);

            BigDecimal amountToSettle = debtor.balance.min(creditor.balance).setScale(2, RoundingMode.HALF_UP);

            if (amountToSettle.compareTo(BigDecimal.ZERO) > 0) {
                transactions.add(SettlementTransactionResponse.builder()
                        .from(participantDtoMap.get(debtor.id))
                        .to(participantDtoMap.get(creditor.id))
                        .amount(amountToSettle)
                        .build());
            }

            debtor.balance = debtor.balance.subtract(amountToSettle);
            creditor.balance = creditor.balance.subtract(amountToSettle);

            if (debtor.balance.compareTo(BigDecimal.ZERO) == 0) {
                i++;
            }
            if (creditor.balance.compareTo(BigDecimal.ZERO) == 0) {
                j++;
            }
        }

        return transactions;
    }
}
