package com.allobankdev.splitbill.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponseDTO {
    private String id;
    private String groupId;
    private String description;
    private String paidBy;
    private BigDecimal amount;
    private List<String> splitAmong;
}
