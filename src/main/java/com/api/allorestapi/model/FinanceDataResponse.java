package com.api.allorestapi.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FinanceDataResponse {

    private String resourceType;

    private List<Object> data;
}
