package com.allobank.splitbill.api.dto.response;

import com.allobank.splitbill.domain.BillStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record GroupBillSummaryResponse(
        UUID groupId,
        String groupName,
        String currency,
        UUID participantId,
        String participantUsername,
        BigDecimal balance,
        BigDecimal youOwe,
        BigDecimal youAreOwed,
        BillStatus status,
        BigDecimal totalExpenses,
        List<TransferResponse> transfers
) {
}
