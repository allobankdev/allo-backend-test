package com.example.tech.splitbiller.model.response;

import java.math.BigDecimal;

public record PersonBalanceResponse(
        String personId,
        String personName,
        BigDecimal balance
) {}
