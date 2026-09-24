package com.example.tech.splitbiller.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreatePersonRequest(
        @NotBlank(message = "Please provide name")
        String name,

        @NotBlank(message = "Please provide email")
        @Email
        String email
) {}
