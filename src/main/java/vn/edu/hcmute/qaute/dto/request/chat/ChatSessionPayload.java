package vn.edu.hcmute.qaute.dto.request.chat;

import jakarta.validation.constraints.NotNull;

public record ChatSessionPayload(@NotNull Long sessionId) {
}
