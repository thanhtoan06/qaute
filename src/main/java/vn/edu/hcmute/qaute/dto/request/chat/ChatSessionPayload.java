package vn.edu.hcmute.qaute.dto.request.chat;

import jakarta.validation.constraints.NotNull;

// Đóng gói dữ liệu đầu vào cho thao tác ChatSessionPayload.
public record ChatSessionPayload(@NotNull Long sessionId) {
}
