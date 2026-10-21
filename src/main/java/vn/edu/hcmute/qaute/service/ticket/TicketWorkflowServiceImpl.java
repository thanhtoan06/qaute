package vn.edu.hcmute.qaute.service.ticket;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.ActorType;
import vn.edu.hcmute.qaute.common.constant.AssignmentType;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ConflictException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.HtmlSanitizerUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.dto.view.SlaSnapshot;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.entity.ticket.TicketAssignment;
import vn.edu.hcmute.qaute.entity.ticket.TicketAttachment;
import vn.edu.hcmute.qaute.entity.ticket.TicketEscalation;
import vn.edu.hcmute.qaute.entity.ticket.TicketMessage;
import vn.edu.hcmute.qaute.entity.ticket.TicketStatusHistory;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketAssignmentRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketAttachmentRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketEscalationRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketMessageRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketStatusHistoryRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.media.MediaService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.system.SettingsService;
import vn.edu.hcmute.qaute.service.ticket.command.CreateTicketCommand;
import vn.edu.hcmute.qaute.service.ticket.event.TicketEventPublisher;
import vn.edu.hcmute.qaute.service.ticket.event.TicketEventType;

@Service
@Transactional
public class TicketWorkflowServiceImpl implements TicketWorkflowService {
    private static final Set<TicketStatus> URGENT_OPEN_STATUSES = Set.of(
            TicketStatus.NEW, TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS, TicketStatus.WAITING_STUDENT);
    private static final Set<TicketStatus> ASSIGNABLE_STATUSES = Set.of(
            TicketStatus.NEW, TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS, TicketStatus.WAITING_STUDENT);
    private static final Set<TicketStatus> OPEN_STATUSES = Set.of(
            TicketStatus.NEW, TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS, TicketStatus.WAITING_STUDENT, TicketStatus.RESOLVED);
    private static final int BATCH_SIZE = 100;
    private final TicketRepository tickets;
    private final TicketMessageRepository messages;
    private final TicketAttachmentRepository attachments;
    private final TicketStatusHistoryRepository histories;
    private final TicketAssignmentRepository assignments;
    private final TicketEscalationRepository escalations;
    private final MediaAssetRepository assets;
    private final MediaService media;
    private final UserLookupService users;
    private final CategoryQueryService categories;
    private final ManagerScopeService managerScope;
    private final SlaService sla;
    private final TicketCodeGenerator codes;
    private final SettingsService settings;
    private final TicketEventPublisher events;

    public TicketWorkflowServiceImpl(TicketRepository tickets, TicketMessageRepository messages,
                                     TicketAttachmentRepository attachments, TicketStatusHistoryRepository histories,
                                     TicketAssignmentRepository assignments, TicketEscalationRepository escalations,
                                     MediaAssetRepository assets, MediaService media, UserLookupService users,
                                     CategoryQueryService categories, ManagerScopeService managerScope,
                                     SlaService sla, TicketCodeGenerator codes,
                                     SettingsService settings, TicketEventPublisher events) {
        this.tickets = tickets;
        this.messages = messages;
        this.attachments = attachments;
        this.histories = histories;
        this.assignments = assignments;
        this.escalations = escalations;
        this.assets = assets;
        this.media = media;
        this.users = users;
        this.categories = categories;
        this.managerScope = managerScope;
        this.sla = sla;
        this.codes = codes;
        this.settings = settings;
        this.events = events;
    }

    @Override
    public Ticket createTicket(Long studentId, CreateTicketCommand command) {
        try {
            requireActiveStudent(studentId);
            if (command == null) throw new BadRequestException("Dữ liệu tạo ticket không hợp lệ");
            String submitToken = blankToNull(command.submitToken());
            if (submitToken != null) {
                Ticket existing = tickets.findBySubmitToken(submitToken).orElse(null);
                if (existing != null) {
                    if (studentId.equals(existing.getStudentId())) return existing;
                    throw new ConflictException("Mã gửi ticket đã được sử dụng");
                }
            }
            validateCategory(command.categoryId());
            String title = validateTitle(command.title());
            Content description = sanitize(command.descriptionHtml(), 30, "descriptionHtml");
            validateUrgent(studentId, command.urgentRequested(), command.urgentReason());
            List<Long> assetIds = normalizedAssetIds(command.assetIds(), studentId);
            LocalDateTime now = LocalDateTime.now();
            SlaSnapshot snapshot = sla.computeInitial(command.categoryId(), TicketPriority.NORMAL, now);
            Ticket ticket = Ticket.builder().code(codes.next()).studentId(studentId).categoryId(command.categoryId())
                    .title(title).descriptionHtml(description.html()).descriptionText(description.text())
                    .priority(TicketPriority.NORMAL).status(TicketStatus.NEW).urgentRequested(command.urgentRequested())
                    .urgentReason(command.urgentRequested() ? command.urgentReason().trim() : null)
                    .allowPublicFaq(command.allowPublicFaq()).reopenCount(0).waitingReminderCount(0)
                    .slaFirstResponseMinutes(snapshot.firstResponseMinutes())
                    .slaResolutionMinutes(snapshot.resolutionMinutes())
                    .slaFirstResponseDueAt(snapshot.firstResponseDueAt()).slaResolutionDueAt(snapshot.resolutionDueAt())
                    .slaPausedSeconds(0).slaStatus(SlaStatus.ON_TRACK).lastActivityAt(now).submitToken(submitToken).build();
            ticket = tickets.saveAndFlush(ticket);
            addMessage(ticket, studentId, SenderType.STUDENT, description, assetIds);
            recordHistory(ticket, null, TicketStatus.NEW, studentId, ActorType.STUDENT, null);
            autoAssignIfEnabled(ticket);
            events.publish(TicketEventType.CREATED, ticket, studentId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException exception) {
            throw mapOptimisticLock(exception);
        }
    }

    @Override
    public Ticket addStudentReply(Long ticketId, Long studentId, String contentHtml, List<Long> assetIds) {
        try {
            Ticket ticket = findOwnedByStudent(ticketId, studentId);
            if (!studentId.equals(ticket.getStudentId())) throw new ForbiddenException("Bạn không sở hữu ticket này");
            if (ticket.getStatus() == TicketStatus.CLOSED || ticket.getStatus() == TicketStatus.CANCELLED) {
                throw new InvalidTransitionException("Ticket đã kết thúc");
            }
            if (ticket.getStatus() == TicketStatus.RESOLVED) {
                throw new InvalidTransitionException("Vui lòng mở lại ticket trước khi trả lời");
            }
            Content content = sanitize(contentHtml, 1, "contentHtml");
            addMessage(ticket, studentId, SenderType.STUDENT, content, normalizedAssetIds(assetIds, studentId));
            if (ticket.getStatus() == TicketStatus.WAITING_STUDENT) {
                ticket.setStatus(TicketStatus.IN_PROGRESS);
                sla.resume(ticket, LocalDateTime.now());
                recordHistory(ticket, TicketStatus.WAITING_STUDENT, TicketStatus.IN_PROGRESS, studentId,
                        ActorType.STUDENT, null);
            }
            touch(ticket);
            ticket = tickets.saveAndFlush(ticket);
            events.publish(TicketEventType.STUDENT_REPLIED, ticket, studentId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException exception) {
            throw mapOptimisticLock(exception);
        }
    }

    @Override
    public Ticket addAdvisorReply(Long ticketId, Long managerId, String contentHtml, List<Long> assetIds) {
        try {
            Ticket ticket = findOrThrow(ticketId);
            if (!managerId.equals(ticket.getCurrentAssigneeId())) throw new ForbiddenException("Ticket không được giao cho bạn");
            if (ticket.getStatus() != TicketStatus.ASSIGNED && ticket.getStatus() != TicketStatus.IN_PROGRESS
                    && ticket.getStatus() != TicketStatus.WAITING_STUDENT) {
                throw new InvalidTransitionException("Trạng thái ticket không cho phép trả lời");
            }
            Content content = sanitize(contentHtml, 1, "contentHtml");
            addMessage(ticket, managerId, SenderType.MANAGER, content, normalizedAssetIds(assetIds, managerId));
            LocalDateTime now = LocalDateTime.now();
            if (ticket.getFirstResponseAt() == null) ticket.setFirstResponseAt(now);
            if (ticket.getStatus() == TicketStatus.ASSIGNED) {
                ticket.setStatus(TicketStatus.IN_PROGRESS);
                recordHistory(ticket, TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS, managerId, ActorType.MANAGER, null);
            }
            touch(ticket);
            ticket = tickets.saveAndFlush(ticket);
            events.publish(TicketEventType.ADVISOR_REPLIED, ticket, managerId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException exception) {
            throw mapOptimisticLock(exception);
        }
    }

    private void recordHistory(Ticket ticket, TicketStatus from, TicketStatus to, Long actorId, ActorType actorType,
                               String reason) {
        histories.save(TicketStatusHistory.builder().ticketId(ticket.getId()).fromStatus(from).toStatus(to)
                .actorId(actorId).actorType(actorType).reason(reason).build());
    }

    private TicketMessage addMessage(Ticket ticket, Long senderId, SenderType senderType, Content content,
                                     List<Long> assetIds) {
        TicketMessage message = messages.save(TicketMessage.builder().ticketId(ticket.getId()).senderId(senderId)
                .senderType(senderType).contentHtml(content.html()).contentText(content.text()).build());
        for (Long assetId : assetIds) {
            media.attach(assetId, MediaUsageType.TICKET_ATTACHMENT, ticket.getId());
            attachments.save(TicketAttachment.builder().ticketId(ticket.getId()).messageId(message.getId())
                    .assetId(assetId).uploaderId(senderId).build());
        }
        return message;
    }

    private void touch(Ticket ticket) {
        ticket.setLastActivityAt(LocalDateTime.now());
    }

    private Ticket findOrThrow(Long id) {
        return tickets.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy ticket"));
    }

    /** Avoid disclosing the existence of a ticket to a different student. */
    private Ticket findOwnedByStudent(Long id, Long studentId) {
        return tickets.findById(id)
                .filter(ticket -> Objects.equals(ticket.getStudentId(), studentId))
                .orElseThrow(NotFoundException::new);
    }

    private ConflictException mapOptimisticLock(OptimisticLockingFailureException exception) {
        return new ConflictException("Ticket vừa được người khác cập nhật, vui lòng tải lại");
    }

    private void requireActiveStudent(Long studentId) {
        UserSummary user = users.getSummary(studentId);
        if (user.role() != RoleCode.STUDENT || user.status() != UserStatus.ACTIVE) {
            throw new BadRequestException("Tài khoản sinh viên không ở trạng thái hoạt động");
        }
    }

    private void validateCategory(Long categoryId) {
        if (categories.findCategory(categoryId).filter(category -> category.active()).isEmpty()) {
            throw new BadRequestException("Chuyên mục không hợp lệ", Map.of("categoryId", "Chuyên mục không tồn tại hoặc đã ngừng hoạt động"));
        }
    }

    private String validateTitle(String raw) {
        String title = raw == null ? "" : raw.trim();
        if (title.length() < 10 || title.length() > 150) {
            throw new BadRequestException("Tiêu đề phải từ 10 đến 150 ký tự", Map.of("title", "Tiêu đề phải từ 10 đến 150 ký tự"));
        }
        return title;
    }

    private Content sanitize(String rawHtml, int minLength, String field) {
        String html = HtmlSanitizerUtil.sanitize(rawHtml);
        String text = TextUtil.stripHtml(html);
        if (text.length() < minLength || text.length() > 5000) {
            throw new BadRequestException("Nội dung không hợp lệ", Map.of(field, "Nội dung phải từ " + minLength + " đến 5000 ký tự"));
        }
        return new Content(html, text);
    }

    private void validateUrgent(Long studentId, boolean urgentRequested, String urgentReason) {
        if (!urgentRequested) return;
        String reason = urgentReason == null ? "" : urgentReason.trim();
        if (reason.length() < 10 || reason.length() > 500) {
            throw new BadRequestException("Lý do khẩn không hợp lệ", Map.of("urgentReason", "Lý do khẩn phải từ 10 đến 500 ký tự"));
        }
        long activeUrgent = tickets.countByStudentIdAndUrgentRequestedTrueAndStatusIn(studentId, URGENT_OPEN_STATUSES);
        if (activeUrgent >= settings.getInt(SettingKeys.TICKET_URGENT_REQUEST_LIMIT)) {
            throw new AppException(ErrorCode.ERR_LIMIT_EXCEEDED, "Bạn đã đạt giới hạn yêu cầu khẩn đang mở");
        }
    }

    private List<Long> normalizedAssetIds(List<Long> input, Long ownerId) {
        List<Long> ids = input == null ? List.of() : new LinkedHashSet<>(input).stream().toList();
        if (ids.size() > settings.getInt(SettingKeys.FILE_MAX_COUNT)) {
            throw new AppException(ErrorCode.ERR_FILE_REJECTED, "Số lượng tệp đính kèm vượt quá giới hạn");
        }
        for (Long assetId : ids) {
            MediaAsset asset = assetId == null ? null : assets.findById(assetId).orElse(null);
            if (asset == null || asset.getStatus() != MediaStatus.ORPHAN
                    || asset.getUsageType() != MediaUsageType.TICKET_ATTACHMENT
                    || !ownerId.equals(asset.getOwnerId())) {
                throw new AppException(ErrorCode.ERR_FILE_REJECTED, "Tệp đính kèm không hợp lệ");
            }
        }
        return ids;
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException("Chưa cài đặt");
    }

    @Override
    public Ticket requestMoreInfo(Long ticketId, Long managerId, String messageHtml, List<Long> assetIds) {
        try {
            Ticket ticket = findOrThrow(ticketId);
            if (!managerId.equals(ticket.getCurrentAssigneeId())) {
                throw new ForbiddenException("Ticket không được giao cho bạn");
            }
            if (ticket.getStatus() != TicketStatus.ASSIGNED && ticket.getStatus() != TicketStatus.IN_PROGRESS) {
                throw new InvalidTransitionException("Chỉ có thể yêu cầu thêm thông tin khi ticket đang được xử lý");
            }
            Content content = sanitize(messageHtml, 1, "messageHtml");
            LocalDateTime now = LocalDateTime.now();
            if (ticket.getFirstResponseAt() == null) ticket.setFirstResponseAt(now);
            if (ticket.getStatus() == TicketStatus.ASSIGNED) {
                recordHistory(ticket, TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS, managerId, ActorType.MANAGER, null);
                ticket.setStatus(TicketStatus.IN_PROGRESS);
            }
            addMessage(ticket, managerId, SenderType.MANAGER, content, normalizedAssetIds(assetIds, managerId));
            ticket.setStatus(TicketStatus.WAITING_STUDENT);
            ticket.setWaitingReminderCount(0);
            sla.pause(ticket, now);
            touch(ticket);
            ticket = tickets.saveAndFlush(ticket);
            recordHistory(ticket, TicketStatus.IN_PROGRESS, TicketStatus.WAITING_STUDENT, managerId, ActorType.MANAGER, null);
            events.publish(TicketEventType.INFO_REQUESTED, ticket, managerId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    public Ticket resolve(Long ticketId, Long managerId, String resolutionNote) {
        try {
            Ticket ticket = findOrThrow(ticketId);
            if (!managerId.equals(ticket.getCurrentAssigneeId())) {
                throw new ForbiddenException("Ticket không được giao cho bạn");
            }
            if (ticket.getStatus() == TicketStatus.ASSIGNED) {
                if (!messages.existsByTicketIdAndSenderType(ticketId, SenderType.MANAGER)) {
                    throw new InvalidTransitionException("Cần có ít nhất một phản hồi trước khi giải quyết");
                }
            } else if (ticket.getStatus() != TicketStatus.IN_PROGRESS) {
                throw new InvalidTransitionException("Chỉ có thể giải quyết ticket đang được xử lý");
            }
            String note = resolutionNote == null ? "" : resolutionNote.trim();
            if (note.length() < 10 || note.length() > 2000) {
                throw new BadRequestException("Ghi chú giải quyết phải từ 10 đến 2000 ký tự");
            }
            LocalDateTime now = LocalDateTime.now();
            TicketStatus prevStatus = ticket.getStatus();
            ticket.setStatus(TicketStatus.RESOLVED);
            ticket.setResolvedAt(now);
            ticket.setResolutionNote(note);
            touch(ticket);
            ticket = tickets.saveAndFlush(ticket);
            recordHistory(ticket, prevStatus, TicketStatus.RESOLVED, managerId, ActorType.MANAGER, null);
            addSystemMessage(ticket, "Yêu cầu đã được giải quyết");
            events.publish(TicketEventType.RESOLVED, ticket, managerId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    public Ticket confirmClose(Long ticketId, Long studentId) {
        try {
            Ticket ticket = findOwnedByStudent(ticketId, studentId);
            if (!studentId.equals(ticket.getStudentId())) {
                throw new ForbiddenException("Bạn không sở hữu ticket này");
            }
            if (ticket.getStatus() != TicketStatus.RESOLVED) {
                throw new InvalidTransitionException("Chỉ có thể xác nhận đóng ticket đã giải quyết");
            }
            LocalDateTime now = LocalDateTime.now();
            ticket.setStatus(TicketStatus.CLOSED);
            ticket.setClosedAt(now);
            ticket.setCloseReason("STUDENT_CONFIRMED");
            touch(ticket);
            ticket = tickets.saveAndFlush(ticket);
            recordHistory(ticket, TicketStatus.RESOLVED, TicketStatus.CLOSED, studentId, ActorType.STUDENT, null);
            events.publish(TicketEventType.CLOSED_BY_STUDENT, ticket, studentId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    public Ticket reopen(Long ticketId, Long studentId, String reason) {
        try {
            Ticket ticket = findOwnedByStudent(ticketId, studentId);
            if (!studentId.equals(ticket.getStudentId())) {
                throw new ForbiddenException("Bạn không sở hữu ticket này");
            }
            boolean isResolved = ticket.getStatus() == TicketStatus.RESOLVED;
            boolean isClosed = ticket.getStatus() == TicketStatus.CLOSED
                    && !"ADMIN_FORCE".equals(ticket.getCloseReason());
            if (!isResolved && !isClosed) {
                throw new InvalidTransitionException("Chỉ có thể mở lại ticket đã giải quyết hoặc đóng");
            }
            LocalDateTime refTime = ticket.getClosedAt() != null ? ticket.getClosedAt() : ticket.getResolvedAt();
            int windowDays = settings.getInt(SettingKeys.TICKET_REOPEN_WINDOW_DAYS);
            if (refTime == null || refTime.plusDays(windowDays).isBefore(LocalDateTime.now())) {
                throw new InvalidTransitionException("Đã hết thời hạn mở lại ticket");
            }
            int maxReopen = settings.getInt(SettingKeys.TICKET_REOPEN_MAX);
            if (ticket.getReopenCount() >= maxReopen) {
                throw new AppException(ErrorCode.ERR_LIMIT_EXCEEDED, "Bạn đã đạt giới hạn số lần mở lại ticket");
            }
            String trimmed = reason == null ? "" : reason.trim();
            if (trimmed.length() < 10 || trimmed.length() > 500) {
                throw new BadRequestException("Lý do mở lại phải từ 10 đến 500 ký tự");
            }
            TicketStatus prevStatus = ticket.getStatus();
            LocalDateTime now = LocalDateTime.now();
            Content content = new Content("<p>" + trimmed + "</p>", trimmed);
            addMessage(ticket, studentId, SenderType.STUDENT, content, List.of());
            ticket.setResolvedAt(null);
            ticket.setClosedAt(null);
            ticket.setCloseReason(null);
            ticket.setReopenCount(ticket.getReopenCount() + 1);
            Long prevAssigneeId = ticket.getCurrentAssigneeId();
            boolean keepAssignee = prevAssigneeId != null && isManagerActiveAndInScope(prevAssigneeId, ticket.getCategoryId());
            if (keepAssignee) {
                ticket.setStatus(TicketStatus.ASSIGNED);
            } else {
                ticket.setCurrentAssigneeId(null);
                ticket.setStatus(TicketStatus.NEW);
            }
            touch(ticket);
            ticket = tickets.saveAndFlush(ticket);
            recordHistory(ticket, prevStatus, ticket.getStatus(), studentId, ActorType.STUDENT, trimmed);
            sla.recompute(ticket, now);
            events.publish(TicketEventType.REOPENED, ticket, studentId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    public Ticket cancel(Long ticketId, Long studentId, String reason) {
        try {
            Ticket ticket = findOwnedByStudent(ticketId, studentId);
            if (!studentId.equals(ticket.getStudentId())) {
                throw new ForbiddenException("Bạn không sở hữu ticket này");
            }
            if (ticket.getStatus() != TicketStatus.NEW) {
                throw new InvalidTransitionException("Chỉ có thể huỷ ticket chưa được nhận");
            }
            String trimmed = reason == null ? "" : reason.trim();
            if (trimmed.length() > 500) {
                throw new BadRequestException("Lý do huỷ không được vượt quá 500 ký tự");
            }
            LocalDateTime now = LocalDateTime.now();
            ticket.setStatus(TicketStatus.CANCELLED);
            ticket.setClosedAt(now);
            ticket.setCloseReason("STUDENT_CANCELLED");
            touch(ticket);
            ticket = tickets.saveAndFlush(ticket);
            recordHistory(ticket, TicketStatus.NEW, TicketStatus.CANCELLED, studentId, ActorType.STUDENT,
                    trimmed.isEmpty() ? null : trimmed);
            events.publish(TicketEventType.CANCELLED, ticket, studentId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    public Ticket changePriority(Long ticketId, Long actorUserId, TicketPriority priority, String reason) {
        try {
            Ticket ticket = findOrThrow(ticketId);
            requireChangeActor(actorUserId, ticket);
            if (!OPEN_STATUSES.contains(ticket.getStatus())) {
                throw new InvalidTransitionException("Không thể thay đổi ưu tiên của ticket đã kết thúc");
            }
            if (priority == ticket.getPriority()) {
                throw new BadRequestException("Mức ưu tiên mới phải khác mức hiện tại");
            }
            String trimmed = reason == null ? "" : reason.trim();
            if (trimmed.length() < 10 || trimmed.length() > 500) {
                throw new BadRequestException("Lý do thay đổi ưu tiên phải từ 10 đến 500 ký tự");
            }
            TicketPriority prev = ticket.getPriority();
            ticket.setPriority(priority);
            touch(ticket);
            LocalDateTime now = LocalDateTime.now();
            sla.recompute(ticket, now);
            ticket = tickets.saveAndFlush(ticket);
            addSystemMessage(ticket, "Mức ưu tiên đổi từ " + prev.name() + " sang " + priority.name());
            events.publish(TicketEventType.PRIORITY_CHANGED, ticket, actorUserId,
                    Map.of("from", prev.name(), "to", priority.name()));
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    public Ticket changeCategory(Long ticketId, Long actorUserId, Long newCategoryId, String reason) {
        try {
            Ticket ticket = findOrThrow(ticketId);
            requireChangeActor(actorUserId, ticket);
            if (!OPEN_STATUSES.contains(ticket.getStatus())) {
                throw new InvalidTransitionException("Không thể chuyển chuyên mục của ticket đã kết thúc");
            }
            if (categories.findCategory(newCategoryId).filter(c -> c.active()).isEmpty()) {
                throw new BadRequestException("Chuyên mục mới không hợp lệ");
            }
            String trimmed = reason == null ? "" : reason.trim();
            if (trimmed.length() < 10 || trimmed.length() > 500) {
                throw new BadRequestException("Lý do chuyển chuyên mục phải từ 10 đến 500 ký tự");
            }
            Long prevCategoryId = ticket.getCategoryId();
            ticket.setCategoryId(newCategoryId);
            Long currentAssigneeId = ticket.getCurrentAssigneeId();
            if (currentAssigneeId != null && !managerScope.isInScope(currentAssigneeId, newCategoryId)) {
                closeCurrentAssignment(ticketId, LocalDateTime.now());
                ticket.setCurrentAssigneeId(null);
                ticket.setStatus(TicketStatus.NEW);
                recordHistory(ticket, TicketStatus.ASSIGNED, TicketStatus.NEW, actorUserId, ActorType.MANAGER, "Chuyển chuyên mục");
            }
            touch(ticket);
            sla.recompute(ticket, LocalDateTime.now());
            ticket = tickets.saveAndFlush(ticket);
            addSystemMessage(ticket, "Yêu cầu được chuyển sang chuyên mục mới");
            events.publish(TicketEventType.CATEGORY_CHANGED, ticket, actorUserId,
                    Map.of("from", String.valueOf(prevCategoryId), "to", String.valueOf(newCategoryId)));
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    public Ticket escalate(Long ticketId, Long managerId, Long toCategoryId, Long toManagerId, String reason) {
        try {
            Ticket ticket = findOrThrow(ticketId);
            if (!managerId.equals(ticket.getCurrentAssigneeId())) {
                throw new ForbiddenException("Ticket không được giao cho bạn");
            }
            Set<TicketStatus> escalatableStatuses = Set.of(
                    TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS, TicketStatus.WAITING_STUDENT);
            if (!escalatableStatuses.contains(ticket.getStatus())) {
                throw new InvalidTransitionException("Chỉ có thể chuyển cấp ticket đang được xử lý");
            }
            if (categories.findCategory(toCategoryId).filter(c -> c.active()).isEmpty()) {
                throw new BadRequestException("Chuyên mục đích không hợp lệ");
            }
            String trimmed = reason == null ? "" : reason.trim();
            if (trimmed.length() < 10 || trimmed.length() > 500) {
                throw new BadRequestException("Lý do chuyển cấp phải từ 10 đến 500 ký tự");
            }
            Long fromCategoryId = ticket.getCategoryId();
            LocalDateTime now = LocalDateTime.now();
            escalations.save(TicketEscalation.builder()
                    .ticketId(ticketId)
                    .fromManagerId(managerId)
                    .fromCategoryId(fromCategoryId)
                    .toCategoryId(toCategoryId)
                    .toManagerId(toManagerId)
                    .reason(trimmed)
                    .build());
            closeCurrentAssignment(ticketId, now);
            if (ticket.getSlaPausedAt() != null) {
                sla.resume(ticket, now);
            }
            TicketStatus prevStatus = ticket.getStatus();
            ticket.setCategoryId(toCategoryId);
            ticket.setCurrentAssigneeId(null);
            ticket.setStatus(TicketStatus.NEW);
            sla.recompute(ticket, now);
            if (toManagerId != null) {
                if (!managerScope.isInScope(toManagerId, toCategoryId)) {
                    throw new BadRequestException("Tư vấn viên không phụ trách chuyên mục đích");
                }
                if (!managerScope.isAvailableForAssignment(toManagerId)) {
                    throw new AppException(ErrorCode.ERR_LIMIT_EXCEEDED, "Tư vấn viên đích đã đạt giới hạn số yêu cầu");
                }
                ticket.setCurrentAssigneeId(toManagerId);
                if (ticket.getFirstAssignedAt() == null) ticket.setFirstAssignedAt(now);
                ticket.setStatus(TicketStatus.ASSIGNED);
            }
            touch(ticket);
            ticket = tickets.saveAndFlush(ticket);
            recordHistory(ticket, prevStatus, ticket.getStatus(), managerId, ActorType.MANAGER, trimmed);
            if (toManagerId != null) {
                saveAssignment(ticket, null, toManagerId, managerId, AssignmentType.ESCALATION, trimmed, now);
            }
            addSystemMessage(ticket, "Yêu cầu được chuyển cấp tới bộ phận phù hợp");
            events.publish(TicketEventType.ESCALATED, ticket, managerId,
                    Map.of("toCategoryId", String.valueOf(toCategoryId), "toManagerId", toManagerId != null ? String.valueOf(toManagerId) : ""));
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    public Ticket forceClose(Long ticketId, Long adminId, String reason) {
        try {
            UserSummary admin = users.getSummary(adminId);
            if (admin.role() != RoleCode.ADMIN) {
                throw new ForbiddenException("Chỉ Admin mới có thể đóng ticket bắt buộc");
            }
            Ticket ticket = findOrThrow(ticketId);
            if (!OPEN_STATUSES.contains(ticket.getStatus())) {
                throw new InvalidTransitionException("Ticket đã kết thúc");
            }
            String trimmed = reason == null ? "" : reason.trim();
            if (trimmed.length() < 10 || trimmed.length() > 500) {
                throw new BadRequestException("Lý do đóng bắt buộc phải từ 10 đến 500 ký tự");
            }
            LocalDateTime now = LocalDateTime.now();
            TicketStatus prevStatus = ticket.getStatus();
            ticket.setStatus(TicketStatus.CLOSED);
            ticket.setClosedAt(now);
            ticket.setCloseReason("ADMIN_FORCE: " + trimmed);
            touch(ticket);
            ticket = tickets.saveAndFlush(ticket);
            recordHistory(ticket, prevStatus, TicketStatus.CLOSED, adminId, ActorType.MANAGER, trimmed);
            addSystemMessage(ticket, "Yêu cầu đã bị đóng bởi quản trị viên");
            events.publish(TicketEventType.FORCE_CLOSED, ticket, adminId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    @Transactional
    public int autoCloseExpired() {
        LocalDateTime now = LocalDateTime.now();
        int closedCount = 0;
        int resolvedDays = settings.getInt(SettingKeys.TICKET_AUTO_CLOSE_RESOLVED_DAYS);
        LocalDateTime resolvedCutoff = now.minusDays(resolvedDays);
        List<Ticket> expiredResolved = tickets.findExpiredResolved(resolvedCutoff, PageRequest.of(0, BATCH_SIZE));
        for (Ticket t : expiredResolved) {
            t.setStatus(TicketStatus.CLOSED);
            t.setClosedAt(now);
            t.setCloseReason("AUTO_TIMEOUT");
            touch(t);
            tickets.save(t);
            recordHistory(t, TicketStatus.RESOLVED, TicketStatus.CLOSED, null, ActorType.MANAGER, "AUTO_TIMEOUT");
            events.publish(TicketEventType.AUTO_CLOSED, t, null, Map.of());
            closedCount++;
        }
        int waitingDays = settings.getInt(SettingKeys.TICKET_AUTO_CLOSE_WAITING_DAYS);
        LocalDateTime waitingCutoff = now.minusDays(waitingDays);
        List<Ticket> expiredWaiting = tickets.findExpiredWaiting(waitingCutoff, PageRequest.of(0, BATCH_SIZE));
        for (Ticket t : expiredWaiting) {
            t.setStatus(TicketStatus.CLOSED);
            t.setClosedAt(now);
            t.setCloseReason("AUTO_NO_RESPONSE");
            if (t.getSlaPausedAt() != null) sla.resume(t, now);
            touch(t);
            tickets.save(t);
            recordHistory(t, TicketStatus.WAITING_STUDENT, TicketStatus.CLOSED, null, ActorType.MANAGER, "AUTO_NO_RESPONSE");
            events.publish(TicketEventType.AUTO_CLOSED, t, null, Map.of());
            closedCount++;
        }
        LocalDateTime reminder3Cutoff = now.minusDays(3);
        LocalDateTime reminder6Cutoff = now.minusDays(6);
        List<Ticket> needReminder = tickets.findWaitingNeedingReminder(reminder3Cutoff, 2, PageRequest.of(0, BATCH_SIZE));
        for (Ticket t : needReminder) {
            if (t.getWaitingReminderCount() == 0 && t.getLastActivityAt().isBefore(reminder3Cutoff)) {
                t.setWaitingReminderCount(1);
                tickets.save(t);
                events.publish(TicketEventType.REMINDER_WAITING, t, null, Map.of("reminderCount", "1"));
            } else if (t.getWaitingReminderCount() == 1 && t.getLastActivityAt().isBefore(reminder6Cutoff)) {
                t.setWaitingReminderCount(2);
                tickets.save(t);
                events.publish(TicketEventType.REMINDER_WAITING, t, null, Map.of("reminderCount", "2"));
            }
        }
        return closedCount;
    }

    @Override
    public Ticket claim(Long ticketId, Long managerId) {
        try {
            requireActiveManager(managerId);
            Ticket ticket = findOrThrow(ticketId);
            if (!managerScope.isInScope(managerId, ticket.getCategoryId())) {
                throw new NotFoundException("Chuyên mục ticket không thuộc phạm vi của bạn");
            }
            if (!managerScope.isAvailableForAssignment(managerId)) {
                throw new AppException(ErrorCode.ERR_LIMIT_EXCEEDED, "Bạn đã nhận tối đa số yêu cầu đồng thời");
            }
            LocalDateTime now = LocalDateTime.now();
            int updated = tickets.claimIfNew(ticketId, managerId, now);
            if (updated == 0) {
                throw new ConflictException("Yêu cầu vừa được người khác nhận");
            }
            ticket = findOrThrow(ticketId);
            saveAssignment(ticket, null, managerId, managerId, AssignmentType.CLAIM, null, now);
            recordHistory(ticket, TicketStatus.NEW, TicketStatus.ASSIGNED, managerId, ActorType.MANAGER, null);
            events.publish(TicketEventType.ASSIGNED, ticket, managerId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    public Ticket assign(Long ticketId, Long toManagerId, Long actorUserId, String reason) {
        try {
            Ticket ticket = findOrThrow(ticketId);
            requireAssignActor(actorUserId, ticket);
            if (!ASSIGNABLE_STATUSES.contains(ticket.getStatus())) {
                throw new InvalidTransitionException("Trạng thái ticket không cho phép giao lại");
            }
            requireActiveManager(toManagerId);
            if (!managerScope.isInScope(toManagerId, ticket.getCategoryId())) {
                throw new NotFoundException("Tư vấn viên không phụ trách chuyên mục này");
            }
            if (!managerScope.isAvailableForAssignment(toManagerId)) {
                throw new AppException(ErrorCode.ERR_LIMIT_EXCEEDED, "Tư vấn viên đã đạt giới hạn số yêu cầu đồng thời");
            }
            boolean hadAssignee = ticket.getCurrentAssigneeId() != null;
            if (hadAssignee) {
                String trimmed = reason == null ? "" : reason.trim();
                if (trimmed.length() < 10 || trimmed.length() > 500) {
                    throw new BadRequestException("Lý do giao lại phải từ 10 đến 500 ký tự");
                }
            }
            LocalDateTime now = LocalDateTime.now();
            closeCurrentAssignment(ticketId, now);
            Long fromManagerId = ticket.getCurrentAssigneeId();
            AssignmentType type = hadAssignee ? AssignmentType.REASSIGN : AssignmentType.ASSIGN;
            ticket.setCurrentAssigneeId(toManagerId);
            if (ticket.getFirstAssignedAt() == null) ticket.setFirstAssignedAt(now);
            if (ticket.getStatus() == TicketStatus.NEW) {
                ticket.setStatus(TicketStatus.ASSIGNED);
                recordHistory(ticket, TicketStatus.NEW, TicketStatus.ASSIGNED, actorUserId, ActorType.MANAGER, null);
            }
            touch(ticket);
            ticket = tickets.saveAndFlush(ticket);
            saveAssignment(ticket, fromManagerId, toManagerId, actorUserId, type, hadAssignee ? reason.trim() : null, now);
            String assigneeName = users.getSummary(toManagerId).fullName();
            addSystemMessage(ticket, "Yêu cầu được giao cho " + assigneeName);
            events.publish(TicketEventType.ASSIGNED, ticket, actorUserId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    public Ticket returnToQueue(Long ticketId, Long actorUserId, String reason) {
        try {
            Ticket ticket = findOrThrow(ticketId);
            boolean isAssignee = actorUserId.equals(ticket.getCurrentAssigneeId());
            boolean isAdmin = isActorAdmin(actorUserId);
            if (!isAssignee && !isAdmin) {
                throw new ForbiddenException("Bạn không có quyền trả ticket về hàng chờ");
            }
            Set<TicketStatus> allowed = Set.of(TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS, TicketStatus.WAITING_STUDENT);
            if (!allowed.contains(ticket.getStatus())) {
                throw new InvalidTransitionException("Chỉ có thể trả ticket đang được xử lý về hàng chờ");
            }
            String trimmed = reason == null ? "" : reason.trim();
            if (trimmed.length() < 10 || trimmed.length() > 500) {
                throw new BadRequestException("Lý do trả hàng chờ phải từ 10 đến 500 ký tự");
            }
            LocalDateTime now = LocalDateTime.now();
            closeCurrentAssignment(ticketId, now);
            TicketStatus prevStatus = ticket.getStatus();
            if (ticket.getSlaPausedAt() != null) {
                sla.resume(ticket, now);
            }
            ticket.setCurrentAssigneeId(null);
            ticket.setStatus(TicketStatus.NEW);
            touch(ticket);
            ticket = tickets.saveAndFlush(ticket);
            recordHistory(ticket, prevStatus, TicketStatus.NEW, actorUserId, ActorType.MANAGER, trimmed);
            events.publish(TicketEventType.RETURNED_TO_QUEUE, ticket, actorUserId, Map.of());
            return ticket;
        } catch (OptimisticLockingFailureException ex) {
            throw mapOptimisticLock(ex);
        }
    }

    @Override
    public void autoAssignIfEnabled(Ticket ticket) {
        if (ticket == null || ticket.getStatus() != TicketStatus.NEW) return;
        if (!settings.getBoolean(SettingKeys.TICKET_AUTO_ASSIGN_ENABLED)) return;
        List<ManagerOption> candidates = managerScope.findAdvisors(ticket.getCategoryId()).stream()
                .filter(m -> m.status() == AvailabilityStatus.ONLINE && managerScope.isAvailableForAssignment(m.id()))
                .collect(java.util.stream.Collectors.toList());
        if (candidates.isEmpty()) return;
        ManagerOption chosen = candidates.stream()
                .min(Comparator.comparingInt((ManagerOption m) -> managerScope.currentOpenTicketCount(m.id()))
                        .thenComparingLong(ManagerOption::id))
                .orElse(null);
        if (chosen == null) return;
        LocalDateTime now = LocalDateTime.now();
        tickets.claimIfNew(ticket.getId(), chosen.id(), now);
        Ticket updated = findOrThrow(ticket.getId());
        saveAssignment(updated, null, chosen.id(), null, AssignmentType.AUTO, null, now);
        recordHistory(updated, TicketStatus.NEW, TicketStatus.ASSIGNED, chosen.id(), ActorType.MANAGER, null);
        events.publish(TicketEventType.ASSIGNED, updated, chosen.id(), Map.of());
        ticket.setStatus(updated.getStatus());
        ticket.setCurrentAssigneeId(updated.getCurrentAssigneeId());
        ticket.setFirstAssignedAt(updated.getFirstAssignedAt());
    }

    private void requireActiveManager(Long managerId) {
        UserSummary user = users.getSummary(managerId);
        if (user.role() != RoleCode.MANAGER || user.status() != UserStatus.ACTIVE) {
            throw new BadRequestException("Tư vấn viên không ở trạng thái hoạt động");
        }
    }

    private void requireAssignActor(Long actorUserId, Ticket ticket) {
        UserSummary actor = users.getSummary(actorUserId);
        if (actor.role() == RoleCode.ADMIN) return;
        if (actor.role() == RoleCode.MANAGER && actor.status() == UserStatus.ACTIVE) {
            if (actorUserId.equals(ticket.getCurrentAssigneeId())) return;
            if (managerScope.isInScope(actorUserId, ticket.getCategoryId())) return;
        }
        throw new ForbiddenException("Bạn không có quyền giao ticket này");
    }

    private void requireChangeActor(Long actorUserId, Ticket ticket) {
        UserSummary actor = users.getSummary(actorUserId);
        if (actor.role() == RoleCode.ADMIN) return;
        if (actor.role() == RoleCode.MANAGER && actor.status() == UserStatus.ACTIVE) {
            if (actorUserId.equals(ticket.getCurrentAssigneeId())) return;
            if (managerScope.isInScope(actorUserId, ticket.getCategoryId())) return;
        }
        throw new ForbiddenException("Bạn không có quyền thực hiện thao tác này");
    }

    private boolean isActorAdmin(Long actorUserId) {
        try {
            UserSummary actor = users.getSummary(actorUserId);
            return actor.role() == RoleCode.ADMIN;
        } catch (NotFoundException e) {
            return false;
        }
    }

    private void saveAssignment(Ticket ticket, Long fromManagerId, Long toManagerId,
                                Long assignedBy, AssignmentType type, String reason, LocalDateTime now) {
        assignments.save(TicketAssignment.builder()
                .ticketId(ticket.getId())
                .fromManagerId(fromManagerId)
                .toManagerId(toManagerId)
                .assignedBy(assignedBy)
                .type(type)
                .reason(reason)
                .assignedAt(now)
                .build());
    }

    private void closeCurrentAssignment(Long ticketId, LocalDateTime now) {
        assignments.findFirstByTicketIdAndEndedAtIsNullOrderByIdDesc(ticketId)
                .ifPresent(a -> {
                    a.setEndedAt(now);
                    assignments.save(a);
                });
    }

    private void addSystemMessage(Ticket ticket, String text) {
        messages.save(TicketMessage.builder()
                .ticketId(ticket.getId())
                .senderId(null)
                .senderType(SenderType.SYSTEM)
                .contentHtml("<p>" + text + "</p>")
                .contentText(text)
                .build());
    }

    private boolean isManagerActiveAndInScope(Long managerId, Long categoryId) {
        try {
            UserSummary user = users.getSummary(managerId);
            return user.role() == RoleCode.MANAGER
                    && user.status() == UserStatus.ACTIVE
                    && managerScope.isInScope(managerId, categoryId);
        } catch (NotFoundException e) {
            return false;
        }
    }

    private record Content(String html, String text) {
    }
}
