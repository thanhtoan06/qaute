package vn.edu.hcmute.qaute.dto.request.chat;

import jakarta.validation.constraints.NotNull;

public record ChatReadPayload(@NotNull Long sessionId, @NotNull Long lastReadMessageId) {
}
