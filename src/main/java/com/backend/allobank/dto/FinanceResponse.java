package com.backend.allobank.dto;

public record FinanceResponse(
        String resourceType,
        Object data
) {}
