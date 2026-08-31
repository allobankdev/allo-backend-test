package com.allobank.splitbill.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseSplitResponse {
    private Long id;
    private ParticipantResponse participant;
    private BigDecimal shareAmount;
    private BigDecimal sharePercentage;
}
