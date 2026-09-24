package com.example.tech.splitbiller.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateExpenseRequest (
        @NotBlank(message = "Please provide group")
        String groupId,

        @NotBlank(message = "Please provide category")
        String categoryId,

        @NotBlank(message = "Please provide description")
        String description,

        @NotNull(message = "Please provide amount")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotBlank(message = "Please provide payer")
        String paidBy,

        @NotNull
        ExpenseSplitRequest split
) {}
