package vn.edu.hcmute.qaute.dto.request.chat;

import jakarta.validation.constraints.NotNull;

// Đóng gói dữ liệu đầu vào cho thao tác ChatTypingPayload.
public record ChatTypingPayload(@NotNull Long sessionId, @NotNull Boolean isTyping) {
}
