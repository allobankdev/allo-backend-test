package com.allobank.assignment.model;

import java.time.Instant;

public record FinanceDataResponse(String resourceType, Object payload, Instant fetchedAt) {
}
