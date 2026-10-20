package vn.edu.hcmute.qaute.dto.request.chat;

import jakarta.validation.constraints.NotNull;

public record ChatTransferPayload(@NotNull Long sessionId, @NotNull Long toManagerId) {
}
