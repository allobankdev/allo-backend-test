package com.example.tech.splitbiller.controller;

import com.example.tech.splitbiller.model.request.CreateExpenseCategoryRequest;
import com.example.tech.splitbiller.model.request.CreateExpenseRequest;
import com.example.tech.splitbiller.model.request.UpdateExpenseCategoryRequest;
import com.example.tech.splitbiller.model.response.BaseResponse;
import com.example.tech.splitbiller.model.response.ExpenseCategoryResponse;
import com.example.tech.splitbiller.model.response.ExpenseCategorySummaryResponse;
import com.example.tech.splitbiller.service.ExpenseCategoryService;
import com.example.tech.splitbiller.service.ExpenseService;
import com.example.tech.splitbiller.util.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = Constants.API_PATH + Constants.EXPENSE_CATEGORY_PATH)
@Validated
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;
    private final ExpenseService expenseService;

    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService,
                                     ExpenseService expenseService) {
        this.expenseCategoryService = expenseCategoryService;
        this.expenseService = expenseService;
    }

    @GetMapping(value = Constants.GET_PATH)
    public ResponseEntity<BaseResponse<List<ExpenseCategoryResponse>>> getAllExpenseCategories() {

        List<ExpenseCategoryResponse> response = expenseCategoryService.getAllExpenseCategories();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<List<ExpenseCategoryResponse>>builder().success(Boolean.TRUE).message("success").data(response).build());
    }

    @GetMapping(value = Constants.GET_PATH + "/{id}")
    public ResponseEntity<BaseResponse<ExpenseCategoryResponse>> getExpenseCategory(@PathVariable("id") String id) {

        ExpenseCategoryResponse response = expenseCategoryService.getExpenseCategory(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<ExpenseCategoryResponse>builder().success(Boolean.TRUE).message("success").data(response).build());
    }

    @GetMapping(value = Constants.GET_PATH + Constants.SUMMARY_PATH)
    public ResponseEntity<BaseResponse<List<ExpenseCategorySummaryResponse>>> summarizeByCategory() {
        List<ExpenseCategorySummaryResponse> response = expenseService.summarizeByCategory();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<List<ExpenseCategorySummaryResponse>>builder().success(Boolean.TRUE).message("success").data(response).build());
    }

    @GetMapping(value = Constants.GET_PATH + Constants.SUMMARY_PATH + "/{groupId}")
    public ResponseEntity<BaseResponse<List<ExpenseCategorySummaryResponse>>> summarizeByCategoryAndGroup(@PathVariable("groupId") String groupId) {

        List<ExpenseCategorySummaryResponse> response = expenseService.summarizeByCategoryAndGroup(groupId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<List<ExpenseCategorySummaryResponse>>builder().success(Boolean.TRUE).message("success").data(response).build());
    }

    @PostMapping(value = Constants.CREATE_PATH)
    public ResponseEntity<BaseResponse<ExpenseCategoryResponse>> createExpenseCategory(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                                                                       @Valid @RequestBody CreateExpenseCategoryRequest request) {

        ExpenseCategoryResponse response = expenseCategoryService.createExpenseCategory(idempotencyKey, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.<ExpenseCategoryResponse>builder().success(Boolean.TRUE).message("success").data(response).build());
    }

    @PutMapping(value = Constants.UPDATE_PATH)
    public ResponseEntity<BaseResponse<ExpenseCategoryResponse>> updateExpenseCategory(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                                                                       @Valid @RequestBody UpdateExpenseCategoryRequest request) {

        ExpenseCategoryResponse response = expenseCategoryService.updateExpenseCategory(idempotencyKey, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<ExpenseCategoryResponse>builder().success(Boolean.TRUE).message("success").data(response).build());
    }

    @DeleteMapping(value = Constants.DELETE_PATH + "/{id}")
    public ResponseEntity<BaseResponse<String>> deleteExpense(@PathVariable("id") String id) {

        expenseCategoryService.deleteExpense(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<String>builder().success(Boolean.TRUE).message("success").data("success").build());
    }
}
