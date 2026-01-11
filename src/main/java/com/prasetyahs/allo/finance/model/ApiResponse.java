package com.prasetyahs.allo.finance.model;

public record ApiResponse<T>(
        T data,
        boolean status,
        String message) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, true, "Berhasil mengambil data");
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(null, false, message);
    }
}
