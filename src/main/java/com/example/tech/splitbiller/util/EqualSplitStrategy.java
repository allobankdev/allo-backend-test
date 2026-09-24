package com.example.tech.splitbiller.util;

import com.example.tech.splitbiller.entity.Expense;
import com.example.tech.splitbiller.entity.ExpenseShare;
import com.example.tech.splitbiller.exception.DataInvalidException;
import com.example.tech.splitbiller.model.SplitParticipant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class EqualSplitStrategy implements SplitStrategy {

    private final MoneyAllocator moneyAllocator;

    public EqualSplitStrategy(MoneyAllocator moneyAllocator) {
        this.moneyAllocator = moneyAllocator;
    }

    @Override
    public List<ExpenseShare> calculate(Expense expense, List<SplitParticipant> participants) {
        if (participants.isEmpty()) {
            throw new DataInvalidException("At least one participant is required");
        }

        List<BigDecimal> amounts = moneyAllocator.allocate(expense.getAmount(),participants.size());

        List<ExpenseShare> result = new ArrayList<>();

        for (int i = 0; i < participants.size(); i++) {
            SplitParticipant participant = participants.get(i);
            ExpenseShare share = new ExpenseShare();
            share.setExpense(expense);
            share.setPerson(participant.person());
            share.setAmount(amounts.get(i));
            result.add(share);
        }

        return result;
    }
}
