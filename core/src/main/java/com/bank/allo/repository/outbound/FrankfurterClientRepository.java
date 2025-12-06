package com.bank.allo.repository.outbound;

import java.util.Map;

public interface FrankfurterClientRepository {

    Map<String, Object> fetchLatestBaseIdr();

    Map<String, Object> fetchHistoricalIdrUsd();

    Map<String, String> fetchSupportedCurrencies();
}
