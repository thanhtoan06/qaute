package vn.edu.hcmute.qaute.dto.view;

import java.io.InputStream;

// Đóng gói dữ liệu MediaStream để hiển thị hoặc trả về API.
public record MediaStream(InputStream stream, String mimeType, long sizeBytes, String originalName,
                          boolean inline) {
}
