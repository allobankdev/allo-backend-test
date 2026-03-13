package com.allobank.backend.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResult {
    private String resource;
    private Object data;
}