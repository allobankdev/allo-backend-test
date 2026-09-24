package com.example.tech.splitbiller.model.response;

import java.math.BigDecimal;

public record PaymentResponse(
        String id,
        String fromPersonId,
        String fromPersonName,
        String toPersonId,
        String toPersonName,
        BigDecimal amount,
        Long paidAt,
        Long createdAt
) {}
