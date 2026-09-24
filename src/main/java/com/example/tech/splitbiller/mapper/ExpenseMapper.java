package com.example.tech.splitbiller.mapper;

import com.example.tech.splitbiller.entity.Expense;
import com.example.tech.splitbiller.model.response.ExpenseResponse;
import com.example.tech.splitbiller.model.response.ExpenseShareResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseMapper {

    public ExpenseResponse toExpenseResponse(Expense expense) {
        List<ExpenseShareResponse> shares = expense.getShares()
                .stream()
                .map(share -> new ExpenseShareResponse(share.getPerson().getId(), share.getPerson().getName(), share.getAmount()))
                .toList();

        return new ExpenseResponse(expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getPaidBy().getId(),
                expense.getPaidBy().getName(),
                expense.getExpenseCategory().getName(),
                expense.getSplitType(),
                shares);
    }
}
