package com.allobank.splitbill.api.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record SettlementResponse(
        UUID groupId,
        String currency,
        BigDecimal totalSubtotal,
        BigDecimal totalTax,
        BigDecimal totalExpenses,
        int serviceChargePct,
        BigDecimal serviceChargeAmount,
        List<BalanceResponse> balances,
        List<TransferResponse> transfers
) {
}
