package com.allobank.splitbill.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRecordResponse {
    private Long id;
    private ParticipantResponse fromParticipant;
    private ParticipantResponse toParticipant;
    private BigDecimal amount;
    private LocalDateTime paidAt;
    private String notes;
}
