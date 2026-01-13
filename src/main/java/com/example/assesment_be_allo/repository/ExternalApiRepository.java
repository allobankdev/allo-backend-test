package com.example.assesment_be_allo.repository;
import java.util.Map;

public interface ExternalApiRepository {

    Map<String, Object> fetchLatestRates(String base);

    Map<String, Object> fetchHistoricalRates(String startDate, String endDate,
                                             String from, String to);

    Map<String, String> fetchSupportedCurrencies();
}