package com.example.tech.splitbiller.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateGroupRequest(
        @NotBlank(message = "Please provide name")
        String name,

        String description,

        @Size(min = 2, message = "Please provide at least 2 person ids")
        List<@NotBlank(message = "Person id cannot be empty") String> personIds
) {}
