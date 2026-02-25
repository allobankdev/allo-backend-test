package com.allobank.finance.fetcher;

import com.allobank.finance.dto.FinanceDataResponse;


public interface IDRDataFetcher {

    FinanceDataResponse fetch();

    String getResourceType();
}
