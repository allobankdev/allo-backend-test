package com.example.tech.splitbiller.model.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateExpenseCategoryRequest(
        @NotBlank(message = "Please provide id")
        String id,

        @NotBlank(message = "Please provide name")
        String name
) {}
