package com.allobankdev.split_bill_api.settlement;

import com.allobankdev.split_bill_api.entity.Expense;
import com.allobankdev.split_bill_api.entity.ExpenseSplit;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SettlementCalculator {

    private final SettlementOptimizer optimizer;

    public SettlementCalculator(
            SettlementOptimizer optimizer) {

        this.optimizer = optimizer;
    }

    public List<TransactionResult> calculate(
            List<Expense> expenses) {

        Map<String, BigDecimal> balances =
                new HashMap<>();

        for (Expense expense : expenses) {

            String payer =
                    expense.getPaidBy().getName();

            balances.putIfAbsent(
                    payer,
                    BigDecimal.ZERO
            );

            balances.put(
                    payer,
                    balances.get(payer)
                            .add(expense.getAmount())
            );

            for (ExpenseSplit split :
                    expense.getSplits()) {

                String participant =
                        split.getParticipant().getName();

                balances.putIfAbsent(
                        participant,
                        BigDecimal.ZERO
                );

                balances.put(
                        participant,
                        balances.get(participant)
                                .subtract(split.getAmount())
                );
            }
        }

        return optimizer.optimize(balances);
    }
}
