package com.amri.apiintegration.endpoint.impl;

import com.amri.apiintegration.application.cache.FinanceDataInMemoryStore;
import com.amri.apiintegration.application.strategy.IDRDataFetcher;
import com.amri.apiintegration.dto.frankfurter.FinanceResourceResultDto;
import com.amri.apiintegration.endpoint.IFrakturterEndpoint;
import com.amri.apiintegration.exception.ResourceNotFoundException;
import com.amri.apiintegration.util.IResultDTO;
import com.amri.apiintegration.util.ResponseBuilderAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class IFraktureterEndpointImpl implements IFrakturterEndpoint {

    private final Map<String, IDRDataFetcher> dataFetchers;
    private final FinanceDataInMemoryStore inMemoryStore;

    @Override
    public IResultDTO<List<FinanceResourceResultDto>> getFinanceData(String resourceType) {
        IDRDataFetcher fetcher = Optional.ofNullable(dataFetchers.get(resourceType))
                .orElseThrow(() -> new ResourceNotFoundException("Unsupported resourceType: " + resourceType));

        return ResponseBuilderAPI.ok(List.of(inMemoryStore.getByResourceType(fetcher.resourceType())));
    }
}
