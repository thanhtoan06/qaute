package vn.edu.hcmute.qaute.dto.view;

// Đóng gói dữ liệu SessionTokens để hiển thị hoặc trả về API.
public record SessionTokens(
        String accessToken,
        String refreshToken,
        int accessMaxAgeSeconds,
        int refreshMaxAgeSeconds) {
}
