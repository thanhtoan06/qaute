package vn.edu.hcmute.qaute.dto.request.chat;

import jakarta.validation.constraints.NotNull;

public record ChatTypingPayload(@NotNull Long sessionId, @NotNull Boolean isTyping) {
}
