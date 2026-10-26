package vn.edu.hcmute.qaute.dto.view;

// Đóng gói dữ liệu MediaAsset để hiển thị hoặc trả về API.
public record MediaAssetView(Long id, String originalName, String mimeType, long sizeBytes, String publicUrl) {
}
