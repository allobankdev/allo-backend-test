package com.allo.splitbill.service;

import com.allo.splitbill.model.BillGroup;
import com.allo.splitbill.model.Debt;
import com.allo.splitbill.model.Expense;
import com.allo.splitbill.model.SettlementResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SettlementService {

    private final ServiceChargeCalculator serviceChargeCalculator;

    public SettlementService(ServiceChargeCalculator serviceChargeCalculator) {
        this.serviceChargeCalculator = serviceChargeCalculator;
    }

    public SettlementResponse calculate(BillGroup group) {
        BigDecimal totalExpenses = group.getTotalExpenses();
        List<String> participants = group.getParticipants();

        Map<String, BigDecimal> balance = new HashMap<>();
        for (String p : participants) {
            balance.put(p, BigDecimal.ZERO);
        }

        for (Expense expense : group.getExpenses()) {
            BigDecimal amount = expense.getAmount();
            String paidBy = expense.getPaidBy();
            List<String> splitAmong = expense.getSplitAmong();

            balance.merge(paidBy, amount, BigDecimal::add);

            BigDecimal share = amount.divide(BigDecimal.valueOf(splitAmong.size()), 2, RoundingMode.HALF_EVEN);
            for (String person : splitAmong) {
                balance.merge(person, share.negate(), BigDecimal::add);
            }
        }

        List<Debt> debts = minimizeTransactions(balance);

        int serviceChargePct = serviceChargeCalculator.getServiceChargePct();
        BigDecimal serviceChargeAmount = serviceChargeCalculator.calculateServiceChargeAmount(totalExpenses);

        return new SettlementResponse(
                group.getId(),
                group.getName(),
                totalExpenses,
                debts,
                serviceChargePct,
                serviceChargeAmount
        );
    }

    static List<Debt> minimizeTransactions(Map<String, BigDecimal> balance) {
        List<Map.Entry<String, BigDecimal>> creditors = new ArrayList<>();
        List<Map.Entry<String, BigDecimal>> debtors = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : balance.entrySet()) {
            int cmp = entry.getValue().compareTo(BigDecimal.ZERO);
            if (cmp > 0) {
                creditors.add(new java.util.AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            } else if (cmp < 0) {
                debtors.add(new java.util.AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            }
        }

        creditors.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        debtors.sort((a, b) -> a.getValue().compareTo(b.getValue()));

        List<Debt> debts = new ArrayList<>();

        int ci = 0, di = 0;
        while (ci < creditors.size() && di < debtors.size()) {
            Map.Entry<String, BigDecimal> creditor = creditors.get(ci);
            Map.Entry<String, BigDecimal> debtor = debtors.get(di);

            BigDecimal creditAmount = creditor.getValue();
            BigDecimal debtAmount = debtor.getValue().abs();

            int cmp = creditAmount.compareTo(debtAmount);
            BigDecimal settled;

            if (cmp >= 0) {
                settled = debtAmount;
                creditor.setValue(creditAmount.subtract(debtAmount));
                debtor.setValue(BigDecimal.ZERO);
                di++;
                if (cmp == 0) ci++;
            } else {
                settled = creditAmount;
                debtor.setValue(debtor.getValue().add(creditAmount));
                creditor.setValue(BigDecimal.ZERO);
                ci++;
            }

            if (settled.compareTo(BigDecimal.ZERO) > 0) {
                debts.add(new Debt(debtor.getKey(), creditor.getKey(), settled));
            }
        }

        return debts;
    }
}
