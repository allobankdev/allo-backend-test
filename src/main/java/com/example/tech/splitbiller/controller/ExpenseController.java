package com.example.tech.splitbiller.controller;

import com.example.tech.splitbiller.model.request.CreateExpenseRequest;
import com.example.tech.splitbiller.model.response.BaseResponse;
import com.example.tech.splitbiller.model.response.ExpenseResponse;
import com.example.tech.splitbiller.service.ExpenseService;
import com.example.tech.splitbiller.util.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = Constants.API_PATH + Constants.EXPENSE_PATH)
@Validated
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping(value = Constants.CREATE_PATH)
    public ResponseEntity<BaseResponse<ExpenseResponse>> createExpense(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                                                       @Valid @RequestBody CreateExpenseRequest request) {

        ExpenseResponse response = expenseService.createExpense(idempotencyKey, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.<ExpenseResponse>builder().success(Boolean.TRUE).message("success").data(response).build());
    }

    @GetMapping(value = Constants.GET_PATH + "/{groupId}")
    public ResponseEntity<BaseResponse<List<ExpenseResponse>>> getGroupExpenses(@PathVariable("groupId") String groupId) {
        List<ExpenseResponse> response = expenseService.getGroupExpenses(groupId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<List<ExpenseResponse>>builder().success(Boolean.TRUE).message("success").data(response).build());
    }

    @GetMapping(value = Constants.GET_PATH + "/{groupId}" + "/{expenseId}")
    public ResponseEntity<BaseResponse<ExpenseResponse>> getGroupExpense(@PathVariable("groupId") String groupId,
                                                                         @PathVariable("expenseId") String expenseId) {
        ExpenseResponse response = expenseService.getGroupExpense(groupId, expenseId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<ExpenseResponse>builder().success(Boolean.TRUE).message("success").data(response).build());
    }
}
