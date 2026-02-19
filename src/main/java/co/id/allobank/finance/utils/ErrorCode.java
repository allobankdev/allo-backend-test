package co.id.allobank.finance.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_RESOURCE_TYPE(101, "INVALID_RESOURCE_TYPE");

    private final int code;
    private final String message;
}
