package com.example.tech.splitbiller.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotBlank(message = "Please provide group")
        String groupId,

        @NotBlank(message = "Please provide source")
        String fromPersonId,

        @NotBlank(message = "Please provide destination")
        String toPersonId,

        @NotNull
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotNull(message = "Please provide paid time")
        Long paidAt
) {}
