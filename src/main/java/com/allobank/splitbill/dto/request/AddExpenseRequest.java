package com.allobank.splitbill.dto.request;

import com.allobank.splitbill.domain.enums.SplitType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddExpenseRequest {

    @NotBlank(message = "Expense description is required")
    private String description;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;

    @NotNull(message = "Paid by participant ID is required")
    private Long paidByParticipantId;

    private SplitType splitType; // Default is EQUAL if null

    // Optional list of splits. If empty or null and splitType is EQUAL, applies to all participants in group.
    private List<ExpenseSplitRequest> splits;
}
