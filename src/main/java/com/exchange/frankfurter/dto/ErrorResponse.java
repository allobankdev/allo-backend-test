package com.exchange.frankfurter.dto;

public record ErrorResponse(
        String message,
        int status
) {}
