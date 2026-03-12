package com.musfiul.idrrateaggregator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {
    private Integer amount;
    private String base;
    private T rates;
}
