package io.aditsukoco.allobank_test.repositories;

import io.aditsukoco.allobank_test.clients.FrankfurterHTTPClientInterface;
import io.aditsukoco.allobank_test.models.dto.LatestAPIResponseDTO;
import jakarta.annotation.PostConstruct;
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
    private final FrankfurterHTTPClientInterface frankfurterHTTPClientInterface;

    // Memories
    private LatestAPIResponseDTO latestResponseData;

    @PostConstruct
    private void init() {
        LatestAPIResponseDTO data = frankfurterHTTPClientInterface.fetchLatest(1, "IDR", "USD");
        this.setLatestResponseData(data);
    }
}