package vn.edu.hcmute.qaute.dto.view;

import java.io.InputStream;

public record MediaStream(InputStream stream, String mimeType, long sizeBytes, String originalName,
                          boolean inline) {
}
