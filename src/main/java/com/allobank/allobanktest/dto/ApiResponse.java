package com.allobank.allobanktest.dto;

public record ApiResponse<T>(
        String resourceType,
        T data
) {
}
