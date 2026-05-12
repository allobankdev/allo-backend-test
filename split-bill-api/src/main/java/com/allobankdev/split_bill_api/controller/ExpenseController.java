package com.allobankdev.split_bill_api.controller;

import com.allobankdev.split_bill_api.dto.AddExpenseRequest;
import com.allobankdev.split_bill_api.entity.Expense;
import com.allobankdev.split_bill_api.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups/{groupId}/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public Expense addExpense(@PathVariable Long groupId,
            @Valid
            @RequestBody
            AddExpenseRequest request) {

        return expenseService.addExpense(
                groupId,
                request
        );
    }
}
