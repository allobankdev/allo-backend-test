package com.allobank.splitbill.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotBlank @Size(max = 36)
        @Schema(description = "Username of the group member making the payment", example = "@bima") String payer,
        @NotBlank @Size(max = 36)
        @Schema(description = "Username of the group member receiving the payment", example = "@ayu") String receiver,
        @NotNull @DecimalMin("0.01") @Digits(integer = 12, fraction = 2) BigDecimal amount
) {
}
