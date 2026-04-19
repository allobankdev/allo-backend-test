package com.allobank.allobackend.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {
    private String base;
    private String target;
    private Object rate;
    private String message;

    @Builder.Default
    private long timestamp = System.currentTimeMillis();
}
