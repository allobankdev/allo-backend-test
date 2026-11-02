package com.allobank.splitbill.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ShareResponse(
        UUID participantId,
        BigDecimal subtotal,
        BigDecimal taxAmount,
        BigDecimal amount
) {
}
