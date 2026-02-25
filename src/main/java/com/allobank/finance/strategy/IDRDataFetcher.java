package com.allobank.finance.strategy;

import com.allobank.finance.model.FinanceDataResult;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

// Todo : idr data fetcher
public interface IDRDataFetcher {

    // Todo : get resource type
    String getResourceType();

    // Todo : fetch data from Frankfurter API
    List<FinanceDataResult> fetch(WebClient webClient);
}
