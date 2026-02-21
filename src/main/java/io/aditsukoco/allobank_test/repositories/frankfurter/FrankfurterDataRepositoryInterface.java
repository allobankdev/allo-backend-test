package io.aditsukoco.allobank_test.repositories.frankfurter;

import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;


public interface FrankfurterDataRepositoryInterface {
    public LatestAPIResponseDTO getLatestResponseData();
    public void setLatestResponseData(LatestAPIResponseDTO data);
}
