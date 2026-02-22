package io.aditsukoco.allobank_test.repositories.frankfurter;

import io.aditsukoco.allobank_test.clients.frankfurter.FrankfurterHTTPClientInterface;
import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@RequiredArgsConstructor
public class FrankfurterDataRepositoryImpl implements FrankfurterDataRepositoryInterface {
    // Deps
    @Autowired
    protected final FrankfurterHTTPClientInterface frankfurterHTTPClientInterface;

    // Memories
    private LatestAPIResponseDTO latestResponseData;
}