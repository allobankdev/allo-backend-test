package com.allobankdev.split_bill_api.service;

import com.allobankdev.split_bill_api.dto.AddExpenseRequest;
import com.allobankdev.split_bill_api.entity.Expense;

public interface ExpenseService {

    Expense addExpense(
            Long groupId,
            AddExpenseRequest request
    );
}
