package com.allobankdev.splitbill.dto.expense;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequestDTO {
    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "PaidBy is required")
    private String paidBy;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    // Optional: if empty or null, means split among all participants in the group
    private List<String> splitAmong;
}
