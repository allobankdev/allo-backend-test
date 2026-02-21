package io.aditsukoco.allobank_test.repositories;

import io.aditsukoco.allobank_test.models.dto.LatestAPIResponseDTO;


public interface FrankfurterDataRepositoryInterface {
    public LatestAPIResponseDTO getLatestResponseData();
    public void setLatestResponseData(LatestAPIResponseDTO data);
}
