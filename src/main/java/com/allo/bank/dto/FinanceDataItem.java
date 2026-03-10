package com.allo.bank.dto;

import java.util.Map;

public record FinanceDataItem(
    String resourceType,
    Map<String, Object> payload
) {
}
