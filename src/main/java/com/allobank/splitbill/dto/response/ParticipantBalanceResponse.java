package com.allobank.splitbill.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantBalanceResponse {
    private Long participantId;
    private String participantName;
    private BigDecimal totalPaid;
    private BigDecimal totalOwed;
    private BigDecimal netBalance; // positive = creditor (is owed money), negative = debtor (owes money)
}
