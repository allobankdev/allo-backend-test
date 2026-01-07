package com.allobank.aggregator.strategy;

import com.allobank.aggregator.dto.FinanceDataDto;

public interface IDRDataFetcher {
    /**
     * Key that identifies this strategy, must match path variable values:
     * latest_idr_rates, historical_idr_usd, supported_currencies
     */
    String resourceKey();

    /**
     * Fetch data from external API and transform into FinanceDataDto.
     * This will be called by startup runner.
     */
    FinanceDataDto fetch();

    /**
     * Note: Controller will not call fetch(); controller will use the map to validate resourceType,
     * and will serve previously loaded DTOs from the in-memory store.
     */
}
