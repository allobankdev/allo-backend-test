package com.allobank.splitbill.api.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GroupResponse(
        UUID id,
        String name,
        String currency,
        List<ParticipantResponse> participants,
        Instant createdAt
) {
}
