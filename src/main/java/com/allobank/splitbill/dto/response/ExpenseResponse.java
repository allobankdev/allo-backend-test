package com.allobank.splitbill.dto.response;

import com.allobank.splitbill.domain.enums.SplitType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponse {
    private Long id;
    private String description;
    private BigDecimal totalAmount;
    private SplitType splitType;
    private ParticipantResponse paidBy;
    private LocalDateTime createdAt;
    private List<ExpenseSplitResponse> splits;
}
