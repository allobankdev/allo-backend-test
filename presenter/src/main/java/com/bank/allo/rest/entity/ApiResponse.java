package com.bank.allo.rest.entity;

import java.time.ZonedDateTime;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Value
@Builder(builderClassName = "Builder")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    ZonedDateTime timestamp;
    Integer code;
    String message;
    Source source;
    T data;
    Long totalData;

    public static <T> ApiResponse<T> defaultBuilder(Integer code, String message, Source source, T data) {
        Long total = null;

        if (data instanceof Collection<?>) {
            total = (long) ((Collection<?>) data).size();
        } else if (data != null && data.getClass().isArray()) {
            total = (long) java.lang.reflect.Array.getLength(data);
        }

        return ApiResponse.<T>builder()
                .timestamp(ZonedDateTime.now())
                .code(code)
                .message(message)
                .source(source)
                .data(data)
                .totalData(total)
                .build();
    }

    public enum Source {
        APPLICATION,
        UNKNOWN
    }
}
