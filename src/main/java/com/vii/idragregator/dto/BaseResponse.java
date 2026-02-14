package com.vii.idragregator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_ERROR = "ERROR";

    private String status;
    private String message;
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .status(STATUS_SUCCESS)
                .message("Data retrieved successfully")
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> error(String message) {
        return BaseResponse.<T>builder()
                .status(STATUS_ERROR)
                .message(message)
                .data(null)
                .build();
    }
}