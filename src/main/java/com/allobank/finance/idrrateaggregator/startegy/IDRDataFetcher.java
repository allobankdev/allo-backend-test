package com.allobank.finance.idrrateaggregator.startegy;

import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;

import java.util.List;

public interface IDRDataFetcher {

    String getResourceType();

    List<FinanceResponse> fetch();
}
