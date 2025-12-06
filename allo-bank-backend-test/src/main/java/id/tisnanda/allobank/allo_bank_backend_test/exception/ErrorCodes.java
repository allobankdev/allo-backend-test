package id.tisnanda.allobank.allo_bank_backend_test.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCodes {

    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", 404),
    INVALID_REQUEST("INVALID_REQUEST", 400),
    INTERNAL_ERROR("INTERNAL_ERROR", 500);

    private final String code;
    private final int httpStatus;

}
