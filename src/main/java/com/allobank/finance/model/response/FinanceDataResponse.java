package com.allobank.finance.model.response;

import com.allobank.finance.model.BaseResponse;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.time.ZonedDateTime;

@RecordBuilder
public record FinanceDataResponse(
        String resourceType,
        ZonedDateTime fetchDate,
        Object data
) implements BaseResponse {
}
