package com.example.tech.splitbiller.mapper;

import com.example.tech.splitbiller.entity.ExpenseCategory;
import com.example.tech.splitbiller.model.response.ExpenseCategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class ExpenseCategoryMapper {

    public ExpenseCategoryResponse toExpenseCategoryResponse(ExpenseCategory expenseCategory) {
        return new ExpenseCategoryResponse(expenseCategory.getId(), expenseCategory.getName(), expenseCategory.getCreatedAt());
    }
}
