package com.allobank.finance.strategy;

import com.allobank.finance.model.FinanceData;

public interface IDRDataFetcher {

    String getResourceType();

    FinanceData fetchData();
}