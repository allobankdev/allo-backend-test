package org.allobank.com.finanacedataservice.strategy;

import org.allobank.com.finanacedataservice.clent.FrankfurterClient;
import org.allobank.com.finanacedataservice.service.FinanceDataCache;

import java.util.List;

public interface IdrDataFetcher {
    String getResourceType();

    void loadData(FrankfurterClient client, FinanceDataCache cache);

    List<?> getCachedData(FinanceDataCache cache);
}
