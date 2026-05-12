package com.allobankdev.split_bill_api.settlement;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SettlementOptimizer {

    public List<TransactionResult> optimize(
            Map<String, BigDecimal> balances) {

        List<BalanceSheet> creditors = new ArrayList<>();
        List<BalanceSheet> debtors = new ArrayList<>();

        balances.forEach((person, amount) -> {

            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(
                        new BalanceSheet(person, amount)
                );
            } else if (
                    amount.compareTo(BigDecimal.ZERO) < 0) {

                debtors.add(
                        new BalanceSheet(
                                person,
                                amount.abs()
                        )
                );
            }
        });

        List<TransactionResult> results =
                new ArrayList<>();

        int i = 0;
        int j = 0;

        while (i < debtors.size()
                && j < creditors.size()) {

            BalanceSheet debtor = debtors.get(i);
            BalanceSheet creditor = creditors.get(j);

            BigDecimal settled =
                    debtor.getBalance()
                            .min(creditor.getBalance());

            results.add(
                    new TransactionResult(
                            debtor.getParticipant(),
                            creditor.getParticipant(),
                            settled
                    )
            );

            debtor.setBalance(
                    debtor.getBalance()
                            .subtract(settled)
            );

            creditor.setBalance(
                    creditor.getBalance()
                            .subtract(settled)
            );

            if (debtor.getBalance()
                    .compareTo(BigDecimal.ZERO) == 0) {
                i++;
            }

            if (creditor.getBalance()
                    .compareTo(BigDecimal.ZERO) == 0) {
                j++;
            }
        }

        return results;
    }
}
