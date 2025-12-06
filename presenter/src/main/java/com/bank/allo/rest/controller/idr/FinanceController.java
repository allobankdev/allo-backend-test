package com.bank.allo.rest.controller.idr;

import com.bank.allo.domain.idr.HistoricalRates;
import com.bank.allo.domain.idr.LatestRates;
import com.bank.allo.domain.idr.SupportedCurrencies;
import com.bank.allo.exception.BadRequestException;
import com.bank.allo.repository.inbound.DataStore;
import com.bank.allo.rest.entity.ApiResponse;
import com.bank.allo.rest.mapper.FinanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@RequiredArgsConstructor
public class FinanceController implements FinanceResource {

    private final DataStore dataStore;
    private final FinanceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getFinanceData(@PathVariable String resourceType) {

        Object raw = dataStore.get(resourceType);
        if (raw == null) {
            throw new BadRequestException("Unknown resource type: " + resourceType);
        }

        Object response = mapToResponse(raw);

        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully fetched " + resourceType)
                .data(response)
                .build();
    }

    private Object mapToResponse(Object raw) {
        if (raw instanceof LatestRates latest) {
            return mapper.toLatestRatesResponse(latest);
        }
        if (raw instanceof HistoricalRates hist) {
            return mapper.toHistoricalRatesResponse(hist);
        }
        if (raw instanceof SupportedCurrencies sup) {
            return mapper.toSupportedCurrenciesResponse(sup);
        }
        throw new BadRequestException("Unsupported domain object");
    }
}
