package com.example.allobank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SpreadDetailDTO {

    private BigDecimal buy;
    private BigDecimal sell;

}

