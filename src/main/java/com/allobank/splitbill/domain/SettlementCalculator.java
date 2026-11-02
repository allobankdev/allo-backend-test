package com.allobank.splitbill.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class SettlementCalculator {
    public record Expense(UUID paidBy, BigDecimal amount, Map<UUID, BigDecimal> shares) {}
    public record Payment(UUID payer, UUID receiver, BigDecimal amount) {}
    public record Result(Map<UUID, BigDecimal> balances, List<Transfer> transfers) {}

    public Result calculate(List<UUID> participants, List<Expense> expenses) {
        return calculate(participants, expenses, List.of());
    }

    public Result calculate(List<UUID> participants, List<Expense> expenses, List<Payment> payments) {
        Map<UUID, BigDecimal> balances = new LinkedHashMap<>();
        participants.forEach(id -> balances.put(id, new BigDecimal("0.00")));
        for (Expense expense : expenses) {
            if (!balances.containsKey(expense.paidBy())) {
                throw new IllegalArgumentException("expense payer is not in the group");
            }
            BigDecimal sharesTotal = expense.shares().values().stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (sharesTotal.compareTo(expense.amount()) != 0) {
                throw new IllegalArgumentException("expense shares do not add up to the amount");
            }
            balances.merge(expense.paidBy(), expense.amount(), BigDecimal::add);
            for (Map.Entry<UUID, BigDecimal> share : expense.shares().entrySet()) {
                if (!balances.containsKey(share.getKey())) {
                    throw new IllegalArgumentException("expense beneficiary is not in the group");
                }
                balances.merge(share.getKey(), share.getValue().negate(), BigDecimal::add);
            }
        }
        for (Payment payment : payments) {
            if (payment == null || !balances.containsKey(payment.payer())
                    || !balances.containsKey(payment.receiver())) {
                throw new IllegalArgumentException("payment parties must be participants in the group");
            }
            if (payment.payer().equals(payment.receiver())) {
                throw new IllegalArgumentException("payment payer and receiver must be different");
            }
            if (payment.amount() == null || payment.amount().signum() <= 0
                    || payment.amount().scale() > 2) {
                throw new IllegalArgumentException(
                        "payment amount must be positive with at most two decimal places");
            }
            balances.merge(payment.payer(), payment.amount(), BigDecimal::add);
            balances.merge(payment.receiver(), payment.amount().negate(), BigDecimal::add);
        }

        List<Account> debtors = new ArrayList<>();
        List<Account> creditors = new ArrayList<>();
        balances.forEach((id, balance) -> {
            if (balance.signum() < 0) debtors.add(new Account(id, balance.negate()));
            if (balance.signum() > 0) creditors.add(new Account(id, balance));
        });
        List<Transfer> transfers = new ArrayList<>();
        int debtorIndex = 0;
        int creditorIndex = 0;
        while (debtorIndex < debtors.size() && creditorIndex < creditors.size()) {
            Account debtor = debtors.get(debtorIndex);
            Account creditor = creditors.get(creditorIndex);
            BigDecimal payment = debtor.remaining.min(creditor.remaining);
            transfers.add(new Transfer(debtor.id, creditor.id, payment));
            debtor.remaining = debtor.remaining.subtract(payment);
            creditor.remaining = creditor.remaining.subtract(payment);
            if (debtor.remaining.signum() == 0) debtorIndex++;
            if (creditor.remaining.signum() == 0) creditorIndex++;
        }
        return new Result(Map.copyOf(balances), List.copyOf(transfers));
    }

    private static final class Account {
        private final UUID id;
        private BigDecimal remaining;

        private Account(UUID id, BigDecimal remaining) {
            this.id = id;
            this.remaining = remaining;
        }
    }
}
