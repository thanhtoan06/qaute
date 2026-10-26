package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import java.util.List;
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

// Đóng gói dữ liệu TicketDetail để hiển thị hoặc trả về API.
public record TicketDetailView(Long id, String code, String title, String categoryPath,
                               TicketStatus status, TicketPriority priority, String assigneeName,
                               String assigneeAvatarUrl, LocalDateTime createdAt,
                               String descriptionHtml, List<MessageView> messages,
                               List<TimelineEvent> timeline, boolean canReply, boolean canCancel,
                               boolean canConfirm, boolean canReopen, boolean canRate,
                               boolean allowPublicFaq, ReviewView review) {
    public record MessageView(Long id, String senderName, SenderType senderType, String contentHtml,
                              LocalDateTime createdAt, List<AttachmentView> attachments) {
    }

    public record AttachmentView(Long assetId, String name, String mimeType, long sizeBytes,
                                 String downloadUrl) {
        // Tạo URL tải xuống mặc định cho tệp đính kèm.
        public AttachmentView(Long assetId, String name, String mimeType, long sizeBytes) {
            this(assetId, name, mimeType, sizeBytes, "/api/media/" + assetId + "/download");
        }
    }

    public record TimelineEvent(LocalDateTime at, String label) {
    }

    public record ReviewView(int rating, List<String> reasonCodes, String comment, boolean editable) {
    }
}
