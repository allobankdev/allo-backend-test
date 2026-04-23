package com.allobank.finnance.allobankfinance.service.strategy;

import com.allobank.finnance.allobankfinance.dto.FinanceRequestDto;

public interface FinanceDataStrategy {

    String getResourceType();

    Object fetchData(FinanceRequestDto  financeRequestDto);
}
