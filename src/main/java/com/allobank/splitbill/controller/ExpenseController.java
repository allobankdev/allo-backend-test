package com.allobank.splitbill.controller;

import com.allobank.splitbill.dto.request.AddExpenseRequest;
import com.allobank.splitbill.dto.response.ExpenseResponse;
import com.allobank.splitbill.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/expenses")
@RequiredArgsConstructor
@Tag(name = "Expenses", description = "Endpoints for managing expenses within a group")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "Add an expense to a group", description = "Adds a shared expense with specified paid_by participant and split strategy (EQUAL, EXACT, PERCENTAGE)")
    public ResponseEntity<ExpenseResponse> addExpense(
            @PathVariable Long groupId,
            @Valid @RequestBody AddExpenseRequest request) {
        ExpenseResponse response = expenseService.addExpense(groupId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List all expenses in a group", description = "Retrieves all expenses recorded for a bill group")
    public ResponseEntity<List<ExpenseResponse>> getExpenses(@PathVariable Long groupId) {
        List<ExpenseResponse> expenses = expenseService.getExpensesByGroup(groupId);
        return ResponseEntity.ok(expenses);
    }
}
