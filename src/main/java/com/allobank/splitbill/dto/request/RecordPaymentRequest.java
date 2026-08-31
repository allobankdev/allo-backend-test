package com.allobank.splitbill.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordPaymentRequest {

    @NotNull(message = "Payer participant ID is required")
    private Long fromParticipantId;

    @NotNull(message = "Recipient participant ID is required")
    private Long toParticipantId;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String notes;
}
