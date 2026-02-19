package co.id.allobank.finance.exception;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.time.ZonedDateTime;

@RecordBuilder
public record ErrorResponse(
        String errorCode,
        String errorDesc,
        ZonedDateTime timestamp
) {
    public static ErrorResponse of(String errorCode, String errorDesc) {
        return new ErrorResponse(
                errorCode,
                errorDesc,
                ZonedDateTime.now()
        );
    }
}
