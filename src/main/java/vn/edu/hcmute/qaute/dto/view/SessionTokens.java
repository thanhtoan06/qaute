package vn.edu.hcmute.qaute.dto.view;

public record SessionTokens(
        String accessToken,
        String refreshToken,
        int accessMaxAgeSeconds,
        int refreshMaxAgeSeconds) {
}
