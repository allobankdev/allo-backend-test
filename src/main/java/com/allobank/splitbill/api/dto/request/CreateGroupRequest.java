package com.allobank.splitbill.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateGroupRequest(
        @NotBlank @Size(max = 120) String name,
        @Pattern(regexp = "^[A-Za-z]{3}$", message = "currency must be a three-letter ISO code")
        String currency,
        @NotEmpty @Size(min = 2, max = 100)
        List<@NotBlank @Pattern(regexp = "^@[A-Za-z0-9_]{3,30}$",
                message = "participant usernames must start with @ and contain 3 to 30 letters, digits, or underscores") String> participants
) {
}
