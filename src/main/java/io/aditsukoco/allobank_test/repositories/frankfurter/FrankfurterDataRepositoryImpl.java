package io.aditsukoco.allobank_test.repositories.frankfurter;

import io.aditsukoco.allobank_test.clients.frankfurter.FrankfurterHTTPClientInterface;
import io.aditsukoco.allobank_test.models.dto.api_response.HistoricalDataAPIResponseDTO;
import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class FrankfurterDataRepositoryImpl implements FrankfurterDataRepositoryInterface {
    // Deps
    @Autowired
    protected final FrankfurterHTTPClientInterface frankfurterHTTPClientInterface;

    // Memories
    private LatestAPIResponseDTO latestResponseData;
    private HistoricalDataAPIResponseDTO historicalResponseData;
    private Map<String, String> currencies;
}