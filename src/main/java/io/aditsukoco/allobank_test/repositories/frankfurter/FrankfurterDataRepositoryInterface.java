package io.aditsukoco.allobank_test.repositories.frankfurter;

import io.aditsukoco.allobank_test.models.dto.api_response.HistoricalDataAPIResponseDTO;
import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;

import java.util.Map;


public interface FrankfurterDataRepositoryInterface {
    LatestAPIResponseDTO getLatestResponseData();
    void setLatestResponseData(LatestAPIResponseDTO data);
    HistoricalDataAPIResponseDTO getHistoricalResponseData();
    void setHistoricalResponseData(HistoricalDataAPIResponseDTO data);
    Map<String, String> getCurrencies();
    void setCurrencies(Map<String, String> data);
}
