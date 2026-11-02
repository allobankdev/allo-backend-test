package com.allobank.splitbill.api.dto.response;

import com.allobank.splitbill.domain.PaymentType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID groupId,
        UUID payer,
        UUID receiver,
        BigDecimal amount,
        PaymentType type,
        UUID reversalOf,
        Instant createdAt
) {
}
