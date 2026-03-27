package com.allobank.allo_backend_test.finance.model;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        String path,
        LocalDateTime timestamp
) {}