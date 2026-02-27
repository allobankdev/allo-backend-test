package com.amri.apiintegration.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseBuilderAPI {
    public static <T> IResultDTO<T> ok(T data) {
        return new IResultDTO<>(HttpStatus.OK.value(), "Success", data);
    }

    public static <T> IResultDTO<T> error(int status, Exception e) {
        e.printStackTrace();
        return new IResultDTO<>(status, e.getMessage(), null);
    }

    public static <T> IResultDTO<T> error(int status, String message) {
        return new IResultDTO<>(status, message, null);
    }
}
