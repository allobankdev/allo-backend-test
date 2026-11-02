package com.allobank.splitbill.api.dto.response;

import java.util.UUID;

public record ParticipantResponse(UUID id, UUID userId, String username, String displayName) {
}
