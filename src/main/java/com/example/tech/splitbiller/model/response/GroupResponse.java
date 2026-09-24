package com.example.tech.splitbiller.model.response;

import java.util.List;

public record GroupResponse (
        String id,
        String name,
        String description,
        Long createdAt,
        List<PersonResponse> members
) {}
