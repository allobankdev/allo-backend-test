package com.chnh16.backendtest.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public abstract class CommonResponse {

    private BigDecimal amount;
    private String base;

}
