package com.example.tech.splitbiller.model.response;

import java.math.BigDecimal;

public record ExpenseCategorySummaryResponse(
        String categoryId,
        String categoryName,
        BigDecimal total
) {}
