package com.allobank.backendtest.dto;

public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;

    private ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data);
    }

    public static ApiResponse failure(String message) {
        return new ApiResponse(false, message, null);
    }

    public boolean isSuccess() { return success; }
    public Object getData() { return data; }
    public String getMessage() { return message; }
}
