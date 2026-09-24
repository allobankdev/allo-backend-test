package com.example.tech.splitbiller.util;

import com.example.tech.splitbiller.entity.Expense;
import com.example.tech.splitbiller.entity.ExpenseShare;
import com.example.tech.splitbiller.exception.DataInvalidException;
import com.example.tech.splitbiller.model.SplitParticipant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class PercentageSplitStrategy implements SplitStrategy {

    private final MoneyAllocator moneyAllocator;

    public PercentageSplitStrategy(MoneyAllocator moneyAllocator) {
        this.moneyAllocator = moneyAllocator;
    }

    @Override
    public List<ExpenseShare> calculate(Expense expense, List<SplitParticipant> participants) {
        if (participants.isEmpty()) {
            throw new DataInvalidException("At least one participant is required");
        }

        List<BigDecimal> percentages = participants.stream()
                .map(SplitParticipant::percentage)
                .toList();

        BigDecimal totalPercentage = BigDecimal.ZERO;

        for (BigDecimal percentage : percentages) {
            if (Objects.isNull(percentage)) {
                throw new DataInvalidException("Percentage is required for PERCENTAGE split");
            }

            if (percentage.compareTo(BigDecimal.ZERO) < 0) {
                throw new DataInvalidException("Percentage cannot be negative");
            }

            totalPercentage = totalPercentage.add(percentage);
        }

        if (totalPercentage.compareTo(new BigDecimal("100")) != 0) {
            throw new DataInvalidException("Percentages must add up to 100");
        }

        List<BigDecimal> amounts = moneyAllocator.allocateByWeight(expense.getAmount(), percentages);

        List<ExpenseShare> shares = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            SplitParticipant participant = participants.get(i);
            ExpenseShare share = new ExpenseShare();
            share.setExpense(expense);
            share.setPerson(participant.person());
            share.setAmount(amounts.get(i));
            shares.add(share);
        }
        return shares;
    }
}
