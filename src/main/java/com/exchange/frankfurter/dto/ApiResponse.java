package com.exchange.frankfurter.dto;

public record ApiResponse<T>(
        String resourceType,
        T data
) {}

