package com.allobank.finance.model.response;

import com.allobank.finance.model.BaseResponse;
import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record SupportedCurrenciesResponse(
        String code,
        String name
) implements BaseResponse {
}
