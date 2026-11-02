package com.allobank.splitbill.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ShareInput(
        @NotBlank @Size(max = 31)
        @Schema(description = "Username of the group member for this share", example = "@kije") String username,
        @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal amount,
        @DecimalMin("0.00") @DecimalMax("100.00") @Digits(integer = 3, fraction = 2) BigDecimal percentage
) {
}
