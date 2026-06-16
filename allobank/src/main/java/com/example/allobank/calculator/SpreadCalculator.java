package com.example.allobank.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.example.allobank.dto.SpreadDetailDTO;

@Component
public class SpreadCalculator {

    public SpreadDetailDTO calculate(BigDecimal rate, BigDecimal spread) {

        BigDecimal baseIdr =
                BigDecimal.ONE.divide(rate, 10, RoundingMode.HALF_UP);

        SpreadDetailDTO dto = new SpreadDetailDTO();

        dto.setBuy(
                baseIdr
                        .multiply(BigDecimal.ONE.add(spread))
                        .setScale(2, RoundingMode.HALF_UP)
        );

        dto.setSell(
                baseIdr
                        .multiply(BigDecimal.ONE.subtract(spread))
                        .setScale(2, RoundingMode.HALF_UP)
        );

        return dto;
    }
}

