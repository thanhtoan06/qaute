package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

public record FaqRevisionView(int versionNo, String title, String summary, String bodyHtml,
                              Long changedBy, String changedByName, String changeNote,
                              LocalDateTime createdAt) {
}
