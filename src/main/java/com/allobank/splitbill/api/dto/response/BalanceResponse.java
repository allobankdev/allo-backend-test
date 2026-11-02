package com.allobank.splitbill.api.dto.response;

import com.allobank.splitbill.domain.BillStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record BalanceResponse(
        UUID participantId,
        String username,
        String displayName,
        BigDecimal balance,
        BigDecimal youOwe,
        BigDecimal youAreOwed,
        BillStatus status
) {
}
