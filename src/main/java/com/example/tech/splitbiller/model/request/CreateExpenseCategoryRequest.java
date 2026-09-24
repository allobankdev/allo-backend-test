package com.example.tech.splitbiller.model.request;

import jakarta.validation.constraints.NotBlank;

public record CreateExpenseCategoryRequest(
        @NotBlank(message = "Please provide name")
        String name
) {}
