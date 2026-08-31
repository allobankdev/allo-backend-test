package com.allobank.splitbill.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseSplitRequest {

    @NotNull(message = "Participant ID is required")
    private Long participantId;

    // Specified for EXACT split
    private BigDecimal amount;

    // Specified for PERCENTAGE split
    private BigDecimal percentage;
}
