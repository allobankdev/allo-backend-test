package com.allobank.splitbill.api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank
        @Pattern(regexp = "^@[A-Za-z0-9_]{3,30}$",
                message = "username must start with @ and contain 3 to 30 letters, digits, or underscores")
        String username,
        @NotBlank @Size(max = 120)
        @JsonProperty("displayName") @JsonAlias("display_name") String displayName
) {
}
