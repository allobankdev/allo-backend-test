package com.allobank.backendtest.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResourceResult(
        @NotBlank String resourceType,
        @NotNull Object data
) {
}
