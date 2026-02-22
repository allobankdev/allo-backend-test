package io.aditsukoco.allobank_test.repositories.frankfurter;

import io.aditsukoco.allobank_test.models.dto.api_response.HistoricalDataAPIResponseDTO;
import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;


public interface FrankfurterDataRepositoryInterface {
    LatestAPIResponseDTO getLatestResponseData();
    void setLatestResponseData(LatestAPIResponseDTO data);
    HistoricalDataAPIResponseDTO getHistoricalResponseData();
    void setHistoricalResponseData(HistoricalDataAPIResponseDTO data);
}
