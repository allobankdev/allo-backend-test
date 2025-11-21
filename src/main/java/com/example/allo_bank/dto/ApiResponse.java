package com.example.allo_bank.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private String resourceType;
    private String status;
    private T data;

}
