package com.allobank.splitbill.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementTransactionResponse {
    private ParticipantResponse from;
    private ParticipantResponse to;
    private BigDecimal amount;
}
