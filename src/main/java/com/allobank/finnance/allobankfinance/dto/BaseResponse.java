package com.allobank.finnance.allobankfinance.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class BaseResponse implements Serializable {


    @Serial
    private static final long serialVersionUID = 7859940117469970809L;

    private String message;
}
