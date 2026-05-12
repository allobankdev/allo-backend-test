package com.allobankdev.split_bill_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SettlementTransactionResponse {

    private String from;
    private String to;
    private BigDecimal amount;
}
