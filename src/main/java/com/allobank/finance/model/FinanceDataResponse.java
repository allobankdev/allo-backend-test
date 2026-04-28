package com.allobank.finance.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinanceDataResponse {

    private final String resourceType;
    private final List<Map<String, Object>> results;
    private final int totalCount;
    private final String fetchedAt;
}