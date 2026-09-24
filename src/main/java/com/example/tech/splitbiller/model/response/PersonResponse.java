package com.example.tech.splitbiller.model.response;

public record PersonResponse(
        String id,
        String name,
        String email,
        Long createdAt
) {}
