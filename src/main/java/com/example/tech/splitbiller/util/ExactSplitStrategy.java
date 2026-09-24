package com.example.tech.splitbiller.util;

import com.example.tech.splitbiller.entity.Expense;
import com.example.tech.splitbiller.entity.ExpenseShare;
import com.example.tech.splitbiller.exception.DataInvalidException;
import com.example.tech.splitbiller.model.SplitParticipant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ExactSplitStrategy implements SplitStrategy{

    @Override
    public List<ExpenseShare> calculate(Expense expense, List<SplitParticipant> participants) {
        if (participants.isEmpty()) {
            throw new DataInvalidException("At least one participant is required");
        }

        BigDecimal total = BigDecimal.ZERO;

        for (SplitParticipant participant : participants) {
            BigDecimal amount = participant.amount();
            if (Objects.isNull(amount)) {
                throw new DataInvalidException("Amount is required for EXACT split");
            }

            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new DataInvalidException("Amount cannot be negative");
            }

            total = total.add(amount);
        }

        if (total.compareTo(expense.getAmount()) != 0) {
            throw new DataInvalidException("Exact split amounts must equal expense amount");
        }

        List<ExpenseShare> shares = new ArrayList<>();

        for (SplitParticipant participant : participants) {
            ExpenseShare share = new ExpenseShare();
            share.setExpense(expense);
            share.setPerson(participant.person());
            share.setAmount(participant.amount().setScale(2, RoundingMode.UNNECESSARY));
            shares.add(share);
        }

        return shares;
    }
}
