package com.allobankdev.split_bill_api.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class SettlementResponse {

    private Long groupId;

    private String groupName;

    private BigDecimal totalExpense;

    private Integer serviceChargePct;

    private BigDecimal serviceChargeAmount;

    private List<SettlementTransactionResponse> transactions;
}
