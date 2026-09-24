package com.example.tech.splitbiller.model.response;

import com.example.tech.splitbiller.util.SplitTypeEnum;

import java.math.BigDecimal;
import java.util.List;

public record ExpenseResponse(
        String id,
        String description,
        BigDecimal amount,
        String paidBy,
        String paidByName,
        String categoryName,
        SplitTypeEnum splitType,
        List<ExpenseShareResponse> shares
) {}
