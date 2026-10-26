package vn.edu.hcmute.qaute.service.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AssignmentType;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.util.HtmlSanitizerUtil;
import vn.edu.hcmute.qaute.dto.view.ManagerTicketDetailView;
import vn.edu.hcmute.qaute.dto.view.ManagerTicketDetailView.AttachmentView;
import vn.edu.hcmute.qaute.dto.view.ManagerTicketDetailView.MessageView;
import vn.edu.hcmute.qaute.dto.view.ManagerTicketDetailView.TicketReviewView;
import vn.edu.hcmute.qaute.dto.view.ManagerTicketDetailView.TicketInfo;
import vn.edu.hcmute.qaute.dto.view.ManagerTicketDetailView.TicketListMini;
import vn.edu.hcmute.qaute.dto.view.ManagerTicketDetailView.TimelineEvent;
import vn.edu.hcmute.qaute.dto.view.StudentSummary;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.entity.ticket.TicketAssignment;
import vn.edu.hcmute.qaute.entity.ticket.TicketAttachment;
import vn.edu.hcmute.qaute.entity.ticket.TicketEscalation;
import vn.edu.hcmute.qaute.entity.ticket.TicketMessage;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketAssignmentRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketAttachmentRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketEscalationRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketMessageRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketReviewRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketStatusHistoryRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;

// Xử lý nghiệp vụ ManagerTicketDetail trong hệ thống.
@Service
@Transactional(readOnly = true)
public class ManagerTicketDetailService {
    private final TicketAccessService access;
    private final TicketRepository tickets;
    private final TicketMessageRepository messages;
    private final TicketAttachmentRepository attachments;
    private final TicketStatusHistoryRepository histories;
    private final TicketAssignmentRepository assignments;
    private final TicketEscalationRepository escalations;
    private final MediaAssetRepository assets;
    private final UserLookupService users;
    private final ManagerScopeService scopes;
    private final CategoryQueryService categories;
    private final FaqQueryService faqs;
    private final TicketNoteService noteService;
    private final TicketReviewRepository reviews;

    // Khởi tạo dịch vụ tổng hợp chi tiết yêu cầu cho tư vấn viên.
    public ManagerTicketDetailService(TicketAccessService access, TicketRepository tickets,
            TicketMessageRepository messages, TicketAttachmentRepository attachments,
            TicketStatusHistoryRepository histories, TicketAssignmentRepository assignments,
            TicketEscalationRepository escalations, MediaAssetRepository assets,
            UserLookupService users, ManagerScopeService scopes,
            CategoryQueryService categories, FaqQueryService faqs,
            TicketNoteService noteService, TicketReviewRepository reviews) {
        this.access = access;
        this.tickets = tickets;
        this.messages = messages;
        this.attachments = attachments;
        this.histories = histories;
        this.assignments = assignments;
        this.escalations = escalations;
        this.assets = assets;
        this.users = users;
        this.scopes = scopes;
        this.categories = categories;
        this.faqs = faqs;
        this.noteService = noteService;
        this.reviews = reviews;
    }

    // Lấy chi tiết yêu cầu với quyền tư vấn viên.
    public ManagerTicketDetailView getDetail(Long managerId, Long ticketId) {
        return getDetail(managerId, ticketId, RoleCode.MANAGER);
    }

    // Lấy chi tiết yêu cầu theo vai trò và quyền truy cập được cấp.
    public ManagerTicketDetailView getDetail(Long userId, Long ticketId, RoleCode role) {
        if (role != RoleCode.MANAGER && role != RoleCode.ADMIN) {
            throw new vn.edu.hcmute.qaute.common.exception.ForbiddenException();
        }
        Ticket ticket = access.requireViewable(ticketId, userId, role);
        StudentSummary student = users.getStudentSummary(ticket.getStudentId());
        List<TicketMessage> ticketMessages = messages.findByTicketIdOrderByCreatedAtAsc(ticketId);
        List<TicketAttachment> ticketAttachments = attachments.findByTicketId(ticketId);
        List<TicketAssignment> ticketAssignments = assignments.findByTicketIdOrderByAssignedAtAsc(ticketId);
        List<TicketEscalation> ticketEscalations = escalations.findByTicketIdOrderByCreatedAtAsc(ticketId);
        Set<Long> userIds = new HashSet<>();
        if (ticket.getCurrentAssigneeId() != null) userIds.add(ticket.getCurrentAssigneeId());
        ticketMessages.stream().map(TicketMessage::getSenderId).filter(id -> id != null)
                .forEach(userIds::add);
        ticketAssignments.stream().map(TicketAssignment::getToManagerId).filter(id -> id != null)
                .forEach(userIds::add);
        ticketEscalations.stream().map(TicketEscalation::getFromManagerId)
                .filter(id -> id != null).forEach(userIds::add);
        ticketEscalations.stream().map(TicketEscalation::getToManagerId)
                .filter(id -> id != null).forEach(userIds::add);
        Map<Long, UserSummary> summaries = userIds.isEmpty() ? Map.of() : users.getSummaries(userIds);
        Map<Long, MediaAsset> media = ticketAttachments.isEmpty() ? Map.of()
                : assets.findAllById(ticketAttachments.stream().map(TicketAttachment::getAssetId)
                        .collect(Collectors.toSet())).stream()
                        .collect(Collectors.toMap(MediaAsset::getId, asset -> asset));
        Map<Long, List<AttachmentView>> attachmentsByMessage = new HashMap<>();
        List<AttachmentView> descriptionAttachments = new ArrayList<>();
        for (TicketAttachment attachment : ticketAttachments) {
            MediaAsset asset = media.get(attachment.getAssetId());
            if (asset == null || asset.getStatus() != MediaStatus.ACTIVE) continue;
            AttachmentView view = new AttachmentView(asset.getId(), asset.getOriginalFilename(),
                    asset.getMimeType(), asset.getSizeBytes(),
                    "/api/media/" + asset.getId() + "/download");
            if (attachment.getMessageId() == null) descriptionAttachments.add(view);
            else attachmentsByMessage.computeIfAbsent(attachment.getMessageId(), ignored -> new ArrayList<>())
                    .add(view);
        }
        List<MessageView> messageViews = ticketMessages.stream().map(message -> new MessageView(
                message.getId(), senderName(message, summaries, student), message.getSenderType(),
                HtmlSanitizerUtil.sanitize(message.getContentHtml()), message.getCreatedAt(),
                attachmentsByMessage.getOrDefault(message.getId(), List.of()))).toList();

        List<TimelineEvent> timeline = new ArrayList<>();
        histories.findByTicketIdOrderByCreatedAtAsc(ticketId).forEach(history -> timeline.add(
                new TimelineEvent(history.getCreatedAt(), "Trạng thái: " + history.getToStatus().getLabel())));
        ticketAssignments.forEach(assignment -> timeline.add(
                new TimelineEvent(assignment.getAssignedAt(), assignmentLabel(assignment, summaries))));
        ticketEscalations.forEach(escalation -> timeline.add(
                new TimelineEvent(escalation.getCreatedAt(), escalationLabel(escalation, summaries))));
        timeline.sort(Comparator.comparing(TimelineEvent::at,
                Comparator.nullsLast(Comparator.naturalOrder())));

        List<Long> scopeIds = role == RoleCode.ADMIN ? List.of() : scopes.getScopeCategoryIds(userId);
        List<Ticket> relatedTickets = role == RoleCode.ADMIN
                ? tickets.findByStudentIdAndIdNotOrderByCreatedAtDescIdDesc(ticket.getStudentId(),
                        ticketId, PageRequest.of(0, 5))
                : scopeIds == null || scopeIds.isEmpty() ? List.of()
                : tickets.findOtherStudentTicketsInScope(ticket.getStudentId(), ticketId,
                        scopeIds, PageRequest.of(0, 5));
        List<TicketListMini> otherTickets = relatedTickets.stream().map(other -> new TicketListMini(
                        other.getId(), other.getCode(), other.getTitle(), other.getStatus(),
                        other.getCreatedAt())).toList();

        boolean mine = userId.equals(ticket.getCurrentAssigneeId());
        boolean inScope = scopeIds != null && scopeIds.contains(ticket.getCategoryId());
        TicketStatus status = ticket.getStatus();
        boolean working = status != null && status.isWorking();
        boolean open = status == TicketStatus.NEW || working || status == TicketStatus.RESOLVED;
        boolean canReply = mine && working;
        TicketInfo info = new TicketInfo(ticket.getId(), ticket.getCode(), ticket.getTitle(),
                HtmlSanitizerUtil.sanitize(ticket.getDescriptionHtml()), ticket.getCategoryId(),
                categories.getDisplayPath(ticket.getCategoryId()), ticket.getPriority(), status,
                ticket.getSlaStatus(), ticket.getSlaFirstResponseDueAt(),
                ticket.getSlaResolutionDueAt(), ticket.getCurrentAssigneeId(),
                name(summaries, ticket.getCurrentAssigneeId()), ticket.isUrgentRequested(),
                ticket.getUrgentReason(), ticket.isAllowPublicFaq(), ticket.getCreatedAt(),
                ticket.getResolutionNote());
        return new ManagerTicketDetailView(info, student, descriptionAttachments, messageViews,
                noteService.listNotes(ticketId, userId, role),
                noteService.listTags(ticketId, userId, role), timeline, otherTickets,
                faqs.suggest(ticket.getTitle(), 5), reviews.findByTicketId(ticketId).map(review ->
                        new TicketReviewView(review.getRating(), review.getReasonCodes(),
                                review.getComment(), review.getCreatedAt())).orElse(null),
                mine, status == TicketStatus.NEW && inScope,
                canReply, mine && (status == TicketStatus.ASSIGNED || status == TicketStatus.IN_PROGRESS),
                mine && (status == TicketStatus.IN_PROGRESS || status == TicketStatus.ASSIGNED
                        && ticketMessages.stream().anyMatch(m -> m.getSenderType() == SenderType.MANAGER)),
                open && (mine || inScope), open && (mine || inScope), mine && working, mine && working);
    }

    // Xác định tên người gửi tin nhắn.
    private static String senderName(TicketMessage message, Map<Long, UserSummary> summaries,
            StudentSummary student) {
        if (message.getSenderType() == SenderType.SYSTEM) return "Hệ thống";
        if (message.getSenderType() == SenderType.STUDENT && student != null
                && student.user() != null) return student.user().fullName();
        return name(summaries, message.getSenderId());
    }

    // Lấy tên người dùng hoặc nhãn mặc định khi chưa phân công.
    private static String name(Map<Long, UserSummary> summaries, Long id) {
        UserSummary summary = id == null ? null : summaries.get(id);
        return summary == null ? "Chưa phân công" : summary.fullName();
    }

    // Tạo nội dung sự kiện tương ứng với loại phân công.
    private static String assignmentLabel(TicketAssignment assignment,
            Map<Long, UserSummary> summaries) {
        AssignmentType type = assignment.getType();
        if (type == AssignmentType.CLAIM) return "Tư vấn viên đã nhận yêu cầu";
        if (type == AssignmentType.ESCALATION) return "Đã phân công sau chuyển cấp";
        return "Đã phân công cho " + name(summaries, assignment.getToManagerId());
    }

    // Tạo nội dung sự kiện chuyển cấp và thông tin người liên quan.
    private String escalationLabel(TicketEscalation escalation, Map<Long, UserSummary> summaries) {
        String from = categories.getDisplayPath(escalation.getFromCategoryId());
        String to = categories.getDisplayPath(escalation.getToCategoryId());
        String actor = escalation.getFromManagerId() == null ? "Không rõ người chuyển"
                : name(summaries, escalation.getFromManagerId());
        String receiver = escalation.getToManagerId() == null ? "hàng đợi"
                : name(summaries, escalation.getToManagerId());
        return "Chuyển cấp từ " + from + " sang " + to + " bởi " + actor
                + "; người nhận: " + receiver + "; lý do: " + escalation.getReason();
    }
}
