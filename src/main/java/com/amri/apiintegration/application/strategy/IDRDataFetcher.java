package com.amri.apiintegration.application.strategy;

import com.amri.apiintegration.dto.frankfurter.FinanceResourceResultDto;

public interface IDRDataFetcher {
    String resourceType();

    FinanceResourceResultDto fetch();
}
