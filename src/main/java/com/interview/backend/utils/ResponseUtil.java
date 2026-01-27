package com.interview.backend.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    public static <T> ResponseEntity<Map<String, Object>> success(T data) {
        return success(data, "Success", HttpStatus.OK);
    }

    public static <T> ResponseEntity<Map<String, Object>> success(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    public static <T> ResponseEntity<Map<String, Object>> success(T data, String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<Map<String, Object>> error(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<Map<String, Object>> error(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("data", null);
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<Map<String, Object>> notFound(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<Map<String, Object>> internalError(String message) {
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
