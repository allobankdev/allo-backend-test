package com.allobank.splitbill.api.dto.response;

import com.allobank.splitbill.domain.SplitType;
import com.allobank.splitbill.domain.TaxMode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ExpenseResponse(
        UUID id,
        UUID groupId,
        String description,
        BigDecimal inputAmount,
        TaxMode taxMode,
        BigDecimal taxRatePct,
        BigDecimal subtotal,
        BigDecimal taxAmount,
        BigDecimal amount,
        UUID paidBy,
        SplitType splitType,
        List<ShareResponse> shares,
        Instant createdAt
) {
}
