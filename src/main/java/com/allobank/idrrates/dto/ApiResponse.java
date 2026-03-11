package com.allobank.idrrates.dto;

import java.time.Instant;
import java.util.List;

public record ApiResponse<T>(
        String resourceType,
        Instant timestamp,
        int count,
        List<T> data
) {

    public static <T> ApiResponse<T> of(String resourceType, List<T> data) {
        return new ApiResponse<>(resourceType, Instant.now(), data.size(), data);
    }
}
