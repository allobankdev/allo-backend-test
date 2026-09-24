package com.example.tech.splitbiller.util;

import com.example.tech.splitbiller.model.response.PersonBalanceResponse;
import com.example.tech.splitbiller.model.response.SettlementResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GreedySettlementOptimizer {

    public List<SettlementResponse> optimize(List<PersonBalanceResponse> balances) {
        List<Balance> debtors = balances.stream()
                .filter(balance -> balance.balance().compareTo(BigDecimal.ZERO) < 0)
                .map(balance -> new Balance(balance.personId(), balance.personName(), balance.balance().negate()))
                .collect(Collectors.toCollection(ArrayList::new));

        List<Balance> creditors = balances.stream()
                .filter(balance -> balance.balance().compareTo(BigDecimal.ZERO) > 0)
                .map(balance -> new Balance(balance.personId(), balance.personName(), balance.balance()))
                .collect(Collectors.toCollection(ArrayList::new));

        List<SettlementResponse> settlements = new ArrayList<>();

        int debtorIndex = 0;
        int creditorIndex = 0;

        while (debtorIndex < debtors.size() && creditorIndex < creditors.size()) {
            Balance debtor = debtors.get(debtorIndex);
            Balance creditor = creditors.get(creditorIndex);

            BigDecimal amount = debtor.amount().min(creditor.amount());

            settlements.add(new SettlementResponse(debtor.personId(), debtor.personName(), creditor.personId(), creditor.personName(), amount));

            BigDecimal remainingDebt = debtor.amount().subtract(amount);

            BigDecimal remainingCredit = creditor.amount().subtract(amount);

            if (remainingDebt.compareTo(BigDecimal.ZERO) == 0) {
                debtorIndex++;
            } else {
                debtors.set(debtorIndex, debtor.withAmount(remainingDebt));
            }

            if (remainingCredit.compareTo(BigDecimal.ZERO) == 0) {
                creditorIndex++;
            } else {
                creditors.set(creditorIndex, creditor.withAmount(remainingCredit));
            }

        }
        return settlements;
    }

    private record Balance(String personId, String personName, BigDecimal amount) {
        Balance withAmount(BigDecimal amount) {
            return new Balance(personId, personName, amount);
        }
    }
}
