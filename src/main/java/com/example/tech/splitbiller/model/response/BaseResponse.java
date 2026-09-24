package com.example.tech.splitbiller.model.response;

import lombok.Builder;

@Builder
public record BaseResponse<T>(
        boolean success,
        String message,
        T data
) {}
