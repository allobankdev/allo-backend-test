package com.example.tech.splitbiller.model.response;

public record CreatePersonResponse(
        String id,
        String name,
        String email,
        Long createdAt
) {}
