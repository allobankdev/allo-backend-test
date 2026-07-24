package com.allobankdev.splitbill.dto.settlement;

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
public class SettlementResponseDTO {
    private String groupId;
    private BigDecimal totalExpenses;
    private int serviceChargePct;
    private BigDecimal serviceChargeAmount;
    private List<TransactionDTO> transactions;
}
