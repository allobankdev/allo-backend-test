package com.allobank.finance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Todo : api response
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private String status;

    private String message;

    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", "Data berhasil diambil", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }
}
