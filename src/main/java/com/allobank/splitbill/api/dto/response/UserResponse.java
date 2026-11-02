package com.allobank.splitbill.api.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(UUID id, String username, String displayName, Instant createdAt) {
}
