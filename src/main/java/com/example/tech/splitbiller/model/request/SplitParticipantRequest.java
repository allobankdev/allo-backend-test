package com.example.tech.splitbiller.model.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record SplitParticipantRequest(
        @NotBlank(message = "Please provide person")
        String personId,

        BigDecimal quantity,

        BigDecimal amount,

        BigDecimal percentage
) {}
