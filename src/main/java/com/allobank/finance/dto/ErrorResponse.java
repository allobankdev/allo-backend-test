package com.allobank.finance.dto;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        String traceId,
        int errorCode,
        String error
) {}
