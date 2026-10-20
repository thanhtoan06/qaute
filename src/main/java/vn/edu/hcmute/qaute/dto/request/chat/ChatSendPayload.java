package vn.edu.hcmute.qaute.dto.request.chat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChatSendPayload(@NotNull Long sessionId, @Size(max = 40) String clientMessageId,
        String content, Long assetId) {
}
