package com.allobankdev.split_bill_api.dto;

import com.allobankdev.split_bill_api.enums.SplitStrategy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AddExpenseRequest {

    @NotBlank
    private String description;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private Long paidBy;

    @NotNull
    private SplitStrategy splitStrategy;

    @NotEmpty
    private List<Long> participantIds;
}
