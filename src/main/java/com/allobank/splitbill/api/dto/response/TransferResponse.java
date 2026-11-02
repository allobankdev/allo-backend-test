package com.allobank.splitbill.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferResponse(
        UUID from,
        UUID to,
        BigDecimal amount,
        String fromUsername,
        String toUsername
) {
}
