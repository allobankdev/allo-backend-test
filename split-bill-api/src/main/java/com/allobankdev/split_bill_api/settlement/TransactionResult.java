package com.allobankdev.split_bill_api.settlement;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransactionResult {

    private String from;
    private String to;
    private BigDecimal amount;
}
