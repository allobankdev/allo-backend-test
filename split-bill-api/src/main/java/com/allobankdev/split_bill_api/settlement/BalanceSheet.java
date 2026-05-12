package com.allobankdev.split_bill_api.settlement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class BalanceSheet {

    private String participant;
    private BigDecimal balance;
}
