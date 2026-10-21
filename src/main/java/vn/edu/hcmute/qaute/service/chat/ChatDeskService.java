package vn.edu.hcmute.qaute.service.chat;

import vn.edu.hcmute.qaute.common.util.BoundedPages;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ConflictException;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.ChatQueueItem;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.dto.view.ManagerChatSessionView;
import vn.edu.hcmute.qaute.dto.view.ChatSessionView;
import vn.edu.hcmute.qaute.dto.view.ChatStudentContextView;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerProfileRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.system.SettingsService;
import vn.edu.hcmute.qaute.websocket.WsSessionRegistry;

@Service
@Transactional
public class ChatDeskService {
    private final ChatSessionRepository sessions;
    private final ManagerScopeService scopes;
    private final ManagerProfileRepository profiles;
    private final ChatAvailabilityService availability;
    private final SettingsService settings;
    private final UserLookupService users;
    private final CategoryQueryService categories;
    private final ChatMessagingService messaging;
    private final ChatEventPublisher publisher;
    private final ApplicationEventPublisher events;
    private final AuditService audit;
    private final EntityManager entityManager;
    private final WsSessionRegistry webSocketSessions;
    private final TicketRepository tickets;
    private final SimpMessagingTemplate socket;

    public ChatDeskService(ChatSessionRepository sessions, ManagerScopeService scopes,
            ManagerProfileRepository profiles, ChatAvailabilityService availability,
            SettingsService settings, UserLookupService users, CategoryQueryService categories,
            ChatMessagingService messaging, ChatEventPublisher publisher,
            ApplicationEventPublisher events, AuditService audit, EntityManager entityManager,
            WsSessionRegistry webSocketSessions, TicketRepository tickets,
            SimpMessagingTemplate socket) {
        this.sessions = sessions;
        this.scopes = scopes;
        this.profiles = profiles;
        this.availability = availability;
        this.settings = settings;
        this.users = users;
        this.categories = categories;
        this.messaging = messaging;
        this.publisher = publisher;
        this.events = events;
        this.audit = audit;
        this.entityManager = entityManager;
        this.webSocketSessions = webSocketSessions;
        this.tickets = tickets;
        this.socket = socket;
    }

    public void accept(Long sessionId, Long managerId) {
        ChatSession session = sessions.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên chat"));
        if (!isActiveAdvisor(managerId, session.getCategoryId())) {
            throw new NotFoundException("Không tìm thấy phiên chat");
        }
        if (session.getStatus() != ChatSessionStatus.WAITING) {
            throw new ConflictException("Phiên đã được tư vấn viên khác nhận");
        }
        // Serialize capacity checks for the same manager across concurrent acceptance requests.
        if (entityManager.find(User.class, managerId, LockModeType.PESSIMISTIC_WRITE) == null) {
            throw new NotFoundException("Không tìm thấy tư vấn viên");
        }
        int max = profiles.findByUserId(managerId).map(profile -> profile.getMaxConcurrentChats())
                .orElseGet(() -> settings.getInt(SettingKeys.CHAT_MAX_CONCURRENT));
        max = Math.min(max, settings.getInt(SettingKeys.CHAT_MAX_CONCURRENT));
        if (sessions.countByManagerIdAndStatus(managerId, ChatSessionStatus.ACTIVE) >= max) {
            throw new BadRequestException("Bạn đã đạt số phiên chat đồng thời tối đa");
        }
        if (sessions.acceptWaiting(sessionId, managerId, LocalDateTime.now()) == 0) {
            throw new ConflictException("Phiên đã được tư vấn viên khác nhận");
        }
        session = sessions.findById(sessionId).orElseThrow();
        UserSummary manager = users.getSummary(managerId);
        String managerName = manager == null ? "Tư vấn viên" : manager.fullName();
        messaging.systemMessage(session, "Tư vấn viên " + managerName + " đã tham gia.");
        publisher.sendToSession(sessionId, Map.of("type", "AGENT_JOINED",
                "managerId", managerId, "managerName", managerName));
        events.publishEvent(new ChatQueueChangedEvent(session.getCategoryId()));
        Long acceptedId = sessionId;
        Runnable acknowledged = () -> socket.convertAndSendToUser(String.valueOf(managerId),
                "/queue/chat-queue", Map.of("type", "CHAT_ACCEPTED", "sessionId", acceptedId));
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() { acknowledged.run(); }
            });
        } else acknowledged.run();
    }

    public void transfer(Long sessionId, Long managerId, Long toManagerId) {
        ChatSession session = sessions.findLockedById(sessionId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên chat"));
        if (!Objects.equals(session.getManagerId(), managerId)) {
            throw new NotFoundException("Không tìm thấy phiên chat");
        }
        if (session.getStatus() != ChatSessionStatus.ACTIVE) {
            throw new InvalidTransitionException("Phiên chat không còn hoạt động");
        }
        if (toManagerId != null && !Objects.equals(managerId, toManagerId)
                && entityManager.find(User.class, toManagerId, LockModeType.PESSIMISTIC_WRITE) == null) {
            throw new BadRequestException("Tư vấn viên nhận không sẵn sàng");
        }
        if (Objects.equals(managerId, toManagerId) || toManagerId == null
                || !availability.eligibleManagerIds(session.getCategoryId()).contains(toManagerId)) {
            throw new BadRequestException("Tư vấn viên nhận không sẵn sàng");
        }
        session.setManagerId(toManagerId);
        sessions.saveAndFlush(session);
        UserSummary manager = users.getSummary(toManagerId);
        String managerName = manager == null ? "Tư vấn viên" : manager.fullName();
        messaging.systemMessage(session, "Phiên chat đã chuyển cho " + managerName + ".");
        publisher.sendToSession(sessionId, Map.of("type", "AGENT_LEFT", "managerId", managerId));
        publisher.sendToSession(sessionId, Map.of("type", "SESSION_TRANSFERRED",
                "managerId", toManagerId, "managerName", managerName));
        audit.log(AuditActions.CHAT_TRANSFERRED, "ChatSession", sessionId,
                "managerId=" + managerId, "managerId=" + toManagerId);
        // A subscription authorized before transfer must not keep receiving private chat events.
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    webSocketSessions.closeAllOf(managerId);
                }
            });
        } else {
            webSocketSessions.closeAllOf(managerId);
        }
    }

    @Transactional(readOnly = true)
    public List<ChatQueueItem> queueSnapshot(Long managerId) {
        return queueSnapshot(managerId, 0);
    }

    @Transactional(readOnly = true)
    public int queueCount(Long managerId) {
        List<Long> ids = scopes.getScopeCategoryIds(managerId);
        return ids.isEmpty() ? 0 : (int) Math.min(Integer.MAX_VALUE,
                sessions.countByStatusAndCategoryIdIn(ChatSessionStatus.WAITING, ids));
    }

    @Transactional(readOnly = true)
    public List<ChatQueueItem> queueSnapshot(Long managerId, int page) {
        List<Long> categoryIds = scopes.getScopeCategoryIds(managerId);
        if (categoryIds.isEmpty()) {
            return List.of();
        }
        List<ChatSession> waiting = sessions.findByStatusAndCategoryIdInOrderByStartedAtAscIdAsc(
                ChatSessionStatus.WAITING, categoryIds, BoundedPages.of(page, 25)).getContent();
        LocalDateTime now = LocalDateTime.now();
        List<ChatQueueItem> result = new ArrayList<>(waiting.size());
        for (ChatSession session : waiting) {
            int position = (int) sessions.queuePosition(ChatSessionStatus.WAITING, session.getCategoryId(),
                    session.getStartedAt(), session.getId());
            UserSummary student = users.getSummary(session.getStudentId());
            result.add(new ChatQueueItem(session.getId(), student == null ? "" : student.fullName(),
                    categories.getDisplayPath(session.getCategoryId()), session.getInitialQuestion(),
                    Math.max(0, Duration.between(session.getLastMessageAt() == null
                            ? session.getStartedAt() : session.getLastMessageAt(), now).toSeconds()), position));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<ManagerChatSessionView> mine(Long managerId) {
        UserSummary manager = users.getSummary(managerId);
        return sessions.findByManagerIdAndStatus(managerId, ChatSessionStatus.ACTIVE).stream()
                .sorted(Comparator.comparing(ChatSession::getLastMessageAt,
                        Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
                .map(session -> new ManagerChatSessionView(new ChatSessionView(session.getId(),
                        session.getStatus(), session.getCategoryId(),
                        categories.getDisplayPath(session.getCategoryId()), session.getInitialQuestion(),
                        managerId, manager == null ? null : manager.fullName(), null,
                        availability.isWorkingNow(), null, List.of(), session.getStartedAt()),
                        studentName(session.getStudentId()),
                        session.getLastMessageAt(), session.getTicketId())).toList();
    }

    @Transactional(readOnly = true)
    public ChatStudentContextView studentContext(Long sessionId, Long managerId) {
        ChatSession session = requireMine(sessionId, managerId);
        List<Long> scopeCategoryIds = scopes.getScopeCategoryIds(managerId);
        List<ChatStudentContextView.RecentTicket> recent = (scopeCategoryIds.isEmpty()
                ? List.<Ticket>of()
                : tickets.findByStudentIdAndCategoryIdInOrderByCreatedAtDescIdDesc(
                        session.getStudentId(), scopeCategoryIds, PageRequest.of(0, 5))).stream()
                .map(ticket -> new ChatStudentContextView.RecentTicket(ticket.getId(),
                        ticket.getCode(), ticket.getTitle(), ticket.getStatus())).toList();
        return new ChatStudentContextView(users.getStudentSummary(session.getStudentId()), recent);
    }

    @Transactional(readOnly = true)
    public List<ManagerOption> transferTargets(Long sessionId, Long managerId) {
        ChatSession session = requireMine(sessionId, managerId);
        List<Long> eligible = availability.eligibleManagerIds(session.getCategoryId());
        return scopes.findAdvisors(session.getCategoryId()).stream()
                .filter(option -> !option.id().equals(managerId) && eligible.contains(option.id()))
                .toList();
    }

    private ChatSession requireMine(Long sessionId, Long managerId) {
        ChatSession session = sessions.findById(sessionId).orElseThrow(NotFoundException::new);
        if (session.getStatus() != ChatSessionStatus.ACTIVE || !Objects.equals(session.getManagerId(), managerId)) {
            throw new NotFoundException();
        }
        return session;
    }

    private String studentName(Long studentId) {
        UserSummary student = users.getSummary(studentId);
        return student == null ? "Sinh viên" : student.fullName();
    }

    private boolean isActiveAdvisor(Long managerId, Long categoryId) {
        return managerId != null && scopes.isInScope(managerId, categoryId)
                && scopes.findAdvisors(categoryId).stream().map(ManagerOption::id)
                        .anyMatch(managerId::equals);
    }
}
