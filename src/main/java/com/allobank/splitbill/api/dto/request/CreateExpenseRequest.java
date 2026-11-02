package com.allobank.splitbill.api.dto.request;

import com.allobank.splitbill.domain.SplitType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record CreateExpenseRequest(
        @NotBlank @Size(max = 240) String description,
        @NotNull @DecimalMin("0.01") @Digits(integer = 12, fraction = 2) BigDecimal amount,
        @Valid TaxInput tax,
        @NotBlank @Size(max = 31)
        @Schema(description = "Username of the group member who paid", example = "@kije") String paidBy,
        @NotNull SplitType splitType,
        @Size(max = 100)
        @Schema(description = "Usernames sharing an equal split; omit to include every group member",
                example = "[\"@ayu\", \"@bima\"]")
        List<@NotBlank @Pattern(regexp = "^@[A-Za-z0-9_]{3,30}$",
                message = "split_between usernames must start with @ and contain 3 to 30 letters, digits, or underscores") String> splitBetween,
        List<@NotNull @Valid ShareInput> shares
) {
}
