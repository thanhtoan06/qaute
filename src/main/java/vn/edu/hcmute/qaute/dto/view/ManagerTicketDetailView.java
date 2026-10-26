package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import java.util.List;
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

// Đóng gói dữ liệu ManagerTicketDetail để hiển thị hoặc trả về API.
public record ManagerTicketDetailView(
        TicketInfo ticket,
        StudentSummary student,
        List<AttachmentView> descriptionAttachments,
        List<MessageView> messages,
        List<TicketInternalNoteView> internalNotes,
        List<TicketTagView> tags,
        List<TimelineEvent> timeline,
        List<TicketListMini> otherTickets,
        List<FaqSuggestion> suggestions,
        TicketReviewView review,
        boolean mine,
        boolean canClaim,
        boolean canReply,
        boolean canRequestInfo,
        boolean canResolve,
        boolean canChangePriority,
        boolean canChangeCategory,
        boolean canEscalate,
        boolean canReturn) {

    public record TicketInfo(Long id, String code, String title, String descriptionHtml,
            Long categoryId, String categoryPath, TicketPriority priority, TicketStatus status,
            SlaStatus slaStatus, LocalDateTime slaFirstResponseDueAt,
            LocalDateTime slaResolutionDueAt, Long assigneeId, String assigneeName,
            boolean urgentRequested, String urgentReason, boolean allowPublicFaq,
            LocalDateTime createdAt, String resolutionNote) { }

    public record MessageView(Long id, String senderName, SenderType senderType,
            String contentHtml, LocalDateTime createdAt, List<AttachmentView> attachments) { }

    public record AttachmentView(Long assetId, String name, String mimeType,
            Long sizeBytes, String downloadUrl) { }

    public record TimelineEvent(LocalDateTime at, String label) { }

    public record TicketListMini(Long id, String code, String title, TicketStatus status,
            LocalDateTime createdAt) { }

    public record TicketReviewView(int rating, String reasonCodes, String comment,
            LocalDateTime createdAt) { }
}
