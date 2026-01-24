package com.sdewa.IdrRateAggregator.dtoes;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    private HttpStatus status;
    private T data;
    private String message;
}
