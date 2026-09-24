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
public class QuantitySplitStrategy implements SplitStrategy {

    private final MoneyAllocator moneyAllocator;

    public QuantitySplitStrategy(MoneyAllocator moneyAllocator) {
        this.moneyAllocator = moneyAllocator;
    }

    @Override
    public List<ExpenseShare> calculate(Expense expense, List<SplitParticipant> participants) {
        if (participants.isEmpty()) {
            throw new DataInvalidException("At least one participant is required");
        }

        List<BigDecimal> quantities = participants.stream()
                .map(SplitParticipant::quantity)
                .toList();

        for (BigDecimal quantity : quantities) {
            if (Objects.isNull(quantity)) {
                throw new DataInvalidException("Quantity is required for QUANTITY split");
            }

            if (quantity.compareTo(BigDecimal.ZERO) < 0) {
                throw new DataInvalidException("Quantity cannot be negative");
            }
        }

        List<BigDecimal> amounts = moneyAllocator.allocateByWeight(expense.getAmount(), quantities);
        List<ExpenseShare> shares = new ArrayList<>();

        for (int i = 0; i < participants.size(); i++) {
            ExpenseShare share = new ExpenseShare();
            share.setExpense(expense);
            share.setPerson(participants.get(i).person());
            share.setAmount(amounts.get(i));
            shares.add(share);
        }
        return shares;
    }
}
