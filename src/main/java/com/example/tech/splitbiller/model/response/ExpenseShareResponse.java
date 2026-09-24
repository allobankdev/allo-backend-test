package com.example.tech.splitbiller.model.response;

import java.math.BigDecimal;

public record ExpenseShareResponse(
        String personId,
        String personName,
        BigDecimal amount
) {}
