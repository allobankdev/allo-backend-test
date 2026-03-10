package com.allo.bank.strategy;

import java.util.List;

import com.allo.bank.dto.FinanceDataItem;

public interface IDRDataFetcher {

    String resourceType();

    List<FinanceDataItem> fetch();
}
