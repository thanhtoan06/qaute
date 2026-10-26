package vn.edu.hcmute.qaute.service.student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.util.HtmlSanitizerUtil;
import vn.edu.hcmute.qaute.dto.view.TicketDetailView;
import vn.edu.hcmute.qaute.dto.view.TicketDetailView.AttachmentView;
import vn.edu.hcmute.qaute.dto.view.TicketDetailView.MessageView;
import vn.edu.hcmute.qaute.dto.view.TicketDetailView.ReviewView;
import vn.edu.hcmute.qaute.dto.view.TicketDetailView.TimelineEvent;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.entity.ticket.TicketAssignment;
import vn.edu.hcmute.qaute.entity.ticket.TicketAttachment;
import vn.edu.hcmute.qaute.entity.ticket.TicketMessage;
import vn.edu.hcmute.qaute.entity.ticket.TicketReview;
import vn.edu.hcmute.qaute.entity.ticket.TicketStatusHistory;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketAssignmentRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketAttachmentRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketMessageRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketReviewRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketStatusHistoryRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.system.SettingsService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;

// Xử lý nghiệp vụ StudentTicketDetail trong hệ thống.
@Service
@Transactional(readOnly = true)
public class StudentTicketDetailService {
    private final TicketAccessService access;
    private final TicketMessageRepository messages;
    private final TicketAttachmentRepository attachments;
    private final TicketStatusHistoryRepository histories;
    private final TicketAssignmentRepository assignments;
    private final MediaAssetRepository assets;
    private final UserLookupService users;
    private final CategoryQueryService categories;
    private final SettingsService settings;
    private final TicketWorkflowService workflow;
    private final TicketReviewRepository reviews;

    // Khởi tạo dịch vụ chi tiết yêu cầu với các phụ thuộc cần thiết.
    public StudentTicketDetailService(TicketAccessService access, TicketMessageRepository messages,
                                      TicketAttachmentRepository attachments, TicketStatusHistoryRepository histories,
                                      TicketAssignmentRepository assignments, MediaAssetRepository assets,
                                      UserLookupService users, CategoryQueryService categories,
                                      SettingsService settings, TicketWorkflowService workflow,
                                      TicketReviewRepository reviews) {
        this.access = access;
        this.messages = messages;
        this.attachments = attachments;
        this.histories = histories;
        this.assignments = assignments;
        this.assets = assets;
        this.users = users;
        this.categories = categories;
        this.settings = settings;
        this.workflow = workflow;
        this.reviews = reviews;
    }

    // Tạo dữ liệu chi tiết yêu cầu hiển thị cho sinh viên.
    public TicketDetailView getDetail(Long studentId, Long ticketId) {
        Ticket ticket = access.requireViewable(ticketId, studentId, RoleCode.STUDENT);
        List<TicketMessage> ticketMessages = messages.findByTicketIdOrderByCreatedAtAsc(ticketId);
        List<TicketAttachment> ticketAttachments = attachments.findByTicketId(ticketId);
        List<TicketAssignment> ticketAssignments = assignments.findByTicketIdOrderByAssignedAtAsc(ticketId);
        Map<Long, UserSummary> summaries = loadSummaries(ticket, ticketMessages, ticketAssignments);
        Map<Long, List<AttachmentView>> attachmentsByMessage = attachmentsByMessage(ticketAttachments);

        List<MessageView> messageViews = ticketMessages.stream().map(message -> new MessageView(
                message.getId(), senderName(message, summaries, studentId), message.getSenderType(),
                HtmlSanitizerUtil.sanitize(message.getContentHtml()), message.getCreatedAt(),
                attachmentsByMessage.getOrDefault(message.getId(), List.of()))).toList();
        List<TimelineEvent> timeline = timeline(ticketId, ticketAssignments, summaries);
        LocalDateTime now = LocalDateTime.now();
        TicketStatus status = ticket.getStatus();
        boolean isResolvedOrClosed = status == TicketStatus.RESOLVED || status == TicketStatus.CLOSED;
        LocalDateTime reopenReference = ticket.getClosedAt() != null ? ticket.getClosedAt() : ticket.getResolvedAt();
        boolean inReopenWindow = reopenReference != null && !reopenReference
                .plusDays(settings.getInt(SettingKeys.TICKET_REOPEN_WINDOW_DAYS)).isBefore(now);
        boolean canReopen = isResolvedOrClosed && !"ADMIN_FORCE".equals(ticket.getCloseReason())
                && inReopenWindow && ticket.getReopenCount() < settings.getInt(SettingKeys.TICKET_REOPEN_MAX);
        TicketReview ticketReview = reviews.findByTicketId(ticketId).orElse(null);
        boolean reviewEditable = ticketReview != null && isReviewEditable(ticketReview, now);
        boolean canRate = isResolvedOrClosed && (ticketReview == null || reviewEditable);
        UserSummary assignee = ticket.getCurrentAssigneeId() == null ? null : summaries.get(ticket.getCurrentAssigneeId());
        return new TicketDetailView(ticket.getId(), ticket.getCode(), ticket.getTitle(),
                categories.getDisplayPath(ticket.getCategoryId()), status, ticket.getPriority(),
                assignee == null ? null : assignee.fullName(), assignee == null ? null : assignee.avatarUrl(),
                ticket.getCreatedAt(), HtmlSanitizerUtil.sanitize(ticket.getDescriptionHtml()), messageViews, timeline,
                status == TicketStatus.NEW || status == TicketStatus.ASSIGNED || status == TicketStatus.IN_PROGRESS
                        || status == TicketStatus.WAITING_STUDENT,
                status == TicketStatus.NEW, status == TicketStatus.RESOLVED, canReopen, canRate,
                ticket.isAllowPublicFaq(), toReviewView(ticketReview, reviewEditable));
    }

    @Transactional
    // Gửi phản hồi của sinh viên vào yêu cầu.
    public void reply(Long studentId, Long ticketId, String html, List<Long> assetIds) {
        workflow.addStudentReply(ticketId, studentId, html, assetIds == null ? List.of() : assetIds);
    }

    // Tính số lần sinh viên còn có thể mở lại yêu cầu.
    public int reopenRemaining(Long studentId, Long ticketId) {
        Ticket ticket = access.requireViewable(ticketId, studentId, RoleCode.STUDENT);
        return Math.max(0, settings.getInt(SettingKeys.TICKET_REOPEN_MAX) - ticket.getReopenCount());
    }

    // Kiểm tra đánh giá còn trong thời hạn chỉnh sửa.
    private boolean isReviewEditable(TicketReview review, LocalDateTime now) {
        return review.getCreatedAt() != null && !review.getCreatedAt()
                .plusDays(settings.getInt(SettingKeys.TICKET_RATING_EDIT_DAYS)).isBefore(now);
    }

    // Chuyển đánh giá sang dữ liệu hiển thị.
    private static ReviewView toReviewView(TicketReview review, boolean editable) {
        if (review == null) return null;
        List<String> reasonCodes = review.getReasonCodes() == null || review.getReasonCodes().isBlank() ? List.of()
                : java.util.Arrays.stream(review.getReasonCodes().split(","))
                        .filter(code -> !code.isBlank()).toList();
        return new ReviewView(review.getRating(), reasonCodes, review.getComment(), editable);
    }

    // Tải thông tin tóm tắt của người dùng liên quan.
    private Map<Long, UserSummary> loadSummaries(Ticket ticket, List<TicketMessage> ticketMessages,
                                                   List<TicketAssignment> ticketAssignments) {
        Set<Long> ids = new HashSet<>();
        if (ticket.getCurrentAssigneeId() != null) ids.add(ticket.getCurrentAssigneeId());
        ticketMessages.stream().map(TicketMessage::getSenderId).filter(java.util.Objects::nonNull).forEach(ids::add);
        ticketAssignments.stream().map(TicketAssignment::getToManagerId)
                .filter(java.util.Objects::nonNull).forEach(ids::add);
        return ids.isEmpty() ? Map.of() : users.getSummaries(ids);
    }

    // Gom tệp đính kèm đang hoạt động theo tin nhắn.
    private Map<Long, List<AttachmentView>> attachmentsByMessage(List<TicketAttachment> ticketAttachments) {
        if (ticketAttachments.isEmpty()) return Map.of();
        Map<Long, MediaAsset> byId = assets.findAllById(ticketAttachments.stream()
                .map(TicketAttachment::getAssetId).collect(Collectors.toSet())).stream()
                .collect(Collectors.toMap(MediaAsset::getId, asset -> asset));
        Map<Long, List<AttachmentView>> result = new HashMap<>();
        for (TicketAttachment attachment : ticketAttachments) {
            MediaAsset asset = byId.get(attachment.getAssetId());
            if (attachment.getMessageId() == null || asset == null || asset.getStatus() != MediaStatus.ACTIVE) continue;
            result.computeIfAbsent(attachment.getMessageId(), ignored -> new ArrayList<>())
                    .add(new AttachmentView(asset.getId(), asset.getOriginalFilename(), asset.getMimeType(), asset.getSizeBytes()));
        }
        return result;
    }

    // Tạo dòng thời gian từ lịch sử trạng thái và phân công.
    private List<TimelineEvent> timeline(Long ticketId, List<TicketAssignment> ticketAssignments,
                                         Map<Long, UserSummary> summaries) {
        List<TimelineEvent> result = new ArrayList<>();
        for (TicketStatusHistory history : histories.findByTicketIdOrderByCreatedAtAsc(ticketId)) {
            result.add(new TimelineEvent(history.getCreatedAt(), statusLabel(history.getToStatus())));
        }
        for (TicketAssignment assignment : ticketAssignments) {
            UserSummary manager = assignment.getToManagerId() == null ? null : summaries.get(assignment.getToManagerId());
            String name = manager == null ? "tư vấn viên" : manager.fullName();
            result.add(new TimelineEvent(assignment.getAssignedAt(), "Đã được nhận bởi " + name));
        }
        result.sort(Comparator.comparing(TimelineEvent::at, Comparator.nullsLast(Comparator.naturalOrder())));
        return result;
    }

    // Xác định tên người gửi hiển thị trong hội thoại.
    private static String senderName(TicketMessage message, Map<Long, UserSummary> summaries, Long studentId) {
        if (message.getSenderType() == SenderType.SYSTEM) return "Hệ thống";
        if (message.getSenderType() == SenderType.STUDENT && studentId.equals(message.getSenderId())) return "Bạn";
        UserSummary user = message.getSenderId() == null ? null : summaries.get(message.getSenderId());
        return user == null ? "Tư vấn viên" : user.fullName();
    }

    // Chuyển trạng thái yêu cầu thành nhãn tiếng Việt.
    private static String statusLabel(TicketStatus status) {
        if (status == null) return "Cập nhật yêu cầu";
        if (status == TicketStatus.NEW) return "Đã gửi yêu cầu";
        if (status == TicketStatus.ASSIGNED) return "Đã được nhận";
        if (status == TicketStatus.IN_PROGRESS) return "Đang xử lý";
        if (status == TicketStatus.WAITING_STUDENT) return "Chờ bạn bổ sung";
        if (status == TicketStatus.RESOLVED) return "Đã giải quyết";
        if (status == TicketStatus.CLOSED) return "Đã đóng";
        if (status == TicketStatus.CANCELLED) return "Đã hủy";
        return "Cập nhật yêu cầu";
    }
}
