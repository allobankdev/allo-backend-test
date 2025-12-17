package com.example.allobank.service;

import com.example.allobank.dto.FinanceDataItemDto;
import java.util.List;

public interface IDRDataFetcher {
    /**
     * Must match incoming resourceType path variable.
     * Example: "latest_idr_rates"
     */
    String resourceType();

    /**
     * Fetch and transform data from external API.
     * Called ONCE at startup by DataLoaderRunner.
     */
    List<FinanceDataItemDto> fetch();
}