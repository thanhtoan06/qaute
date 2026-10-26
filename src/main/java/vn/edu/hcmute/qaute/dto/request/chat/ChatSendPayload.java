package vn.edu.hcmute.qaute.dto.request.chat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Đóng gói dữ liệu đầu vào cho thao tác ChatSendPayload.
public record ChatSendPayload(@NotNull Long sessionId, @Size(max = 40) String clientMessageId,
        String content, Long assetId) {
}
