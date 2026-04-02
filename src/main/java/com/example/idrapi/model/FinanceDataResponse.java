package com.example.idrapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record FinanceDataResponse(
        String resourceType,
        Instant fetchedAt,
        List<Map<String, Object>> results
) {
    public FinanceDataResponse {
        if (resourceType == null || resourceType.isBlank()) {
            throw new IllegalArgumentException("resourceType must not be blank");
        }
        if (results == null) {
            throw new IllegalArgumentException("results must not be null");
        }
        results = List.copyOf(results);
    }
}
