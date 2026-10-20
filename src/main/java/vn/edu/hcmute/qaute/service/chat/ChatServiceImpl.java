package vn.edu.hcmute.qaute.service.chat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import vn.edu.hcmute.qaute.common.constant.ChatContentType;
import vn.edu.hcmute.qaute.common.constant.ChatSenderType;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.dto.view.ChatHistoryItem;
import vn.edu.hcmute.qaute.dto.view.ChatMessageView;
import vn.edu.hcmute.qaute.dto.view.ChatSessionView;
import vn.edu.hcmute.qaute.dto.view.FaqSuggestion;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.chat.ChatMessage;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.chat.ChatMessageRepository;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;
import vn.edu.hcmute.qaute.service.ticket.command.CreateTicketCommand;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {
    private static final Collection<ChatSessionStatus> CURRENT_STATUSES =
            List.of(ChatSessionStatus.BOT, ChatSessionStatus.WAITING, ChatSessionStatus.ACTIVE);
    private final ChatSessionRepository sessions;
    private final ChatMessageRepository messages;
    private final ChatAvailabilityService availability;
    private final CategoryQueryService categories;
    private final FaqQueryService faqs;
    private final UserLookupService users;
    private final NotificationService notifications;
    private final ApplicationEventPublisher events;
    private final EntityManager entityManager;
    private final TicketWorkflowService ticketWorkflow;

    public ChatServiceImpl(ChatSessionRepository sessions, ChatMessageRepository messages,
            ChatAvailabilityService availability, CategoryQueryService categories, FaqQueryService faqs,
            UserLookupService users, NotificationService notifications,
            ApplicationEventPublisher events, EntityManager entityManager,
            TicketWorkflowService ticketWorkflow) {
        this.sessions = sessions;
        this.messages = messages;
        this.availability = availability;
        this.categories = categories;
        this.faqs = faqs;
        this.users = users;
        this.notifications = notifications;
        this.events = events;
        this.entityManager = entityManager;
        this.ticketWorkflow = ticketWorkflow;
    }

    @Override
    public ChatSessionView start(Long studentId, Long categoryId, String question) {
        // Serialize starts by this student so two browser tabs cannot create two active sessions.
        if (entityManager.find(User.class, studentId, LockModeType.PESSIMISTIC_WRITE) == null) {
            throw new NotFoundException("Không tìm thấy sinh viên");
        }
        Optional<ChatSession> existing = sessions.findFirstByStudentIdAndStatusInOrderByIdDesc(
                studentId, CURRENT_STATUSES);
        if (existing.isPresent()) {
            return toView(existing.get(), null);
        }
        if (categoryId == null || categories.findCategory(categoryId).filter(CategoryView::active).isEmpty()) {
            throw new BadRequestException("Chuyên mục không hoạt động");
        }
        String cleanQuestion = trimRequired(question, 5, 500, "Câu hỏi phải dài 5–500 ký tự");
        List<FaqSuggestion> suggestions = faqs.suggest(cleanQuestion, 3);
        LocalDateTime now = LocalDateTime.now();
        ChatSession session = new ChatSession();
        session.setStudentId(studentId);
        session.setCategoryId(categoryId);
        session.setStatus(ChatSessionStatus.BOT);
        session.setInitialQuestion(cleanQuestion);
        session.setStartedAt(now);
        session.setLastMessageAt(now);
        session = sessions.saveAndFlush(session);

        ChatMessage bot = new ChatMessage();
        bot.setSessionId(session.getId());
        bot.setSenderType(ChatSenderType.BOT);
        bot.setContentType(ChatContentType.TEXT);
        bot.setSentAt(now);
        bot.setContent(botContent(suggestions));
        messages.save(bot);
        return toView(session, suggestions);
    }

    @Override
    public ChatSessionView requestAgent(Long sessionId, Long studentId) {
        ChatSession session = requireOwn(sessionId, studentId);
        if (session.getStatus() != ChatSessionStatus.BOT) {
            throw new InvalidTransitionException("Chỉ có thể yêu cầu tư vấn viên từ phiên BOT");
        }
        boolean working = availability.isWorkingNow();
        List<Long> eligible = working ? availability.eligibleManagerIds(session.getCategoryId()) : List.of();
        if (!working || eligible.isEmpty()) {
            session.setStatus(ChatSessionStatus.EXPIRED);
            session.setEndReason("NO_AGENT");
            session.setEndedAt(LocalDateTime.now());
            sessions.saveAndFlush(session);
            return toView(session, List.of(), working,
                    "Hiện chưa có tư vấn viên trực. Bạn có thể chuyển thành yêu cầu tư vấn.");
        }
        session.setStatus(ChatSessionStatus.WAITING);
        session.setLastMessageAt(LocalDateTime.now());
        sessions.saveAndFlush(session);
        events.publishEvent(new ChatQueueChangedEvent(session.getCategoryId()));
        notifications.notifyUsers(eligible, NotificationType.CHAT_WAITING_NEW,
                "Sinh viên đang chờ tư vấn", session.getInitialQuestion(),
                "/manager/chat/queue", "CHAT_SESSION", session.getId());
        return toView(session, List.of(), true, "Đang chờ tư vấn viên tiếp nhận.");
    }

    @Override
    public void cancel(Long sessionId, Long studentId) {
        ChatSession session = requireOwn(sessionId, studentId);
        if (session.getStatus() != ChatSessionStatus.BOT && session.getStatus() != ChatSessionStatus.WAITING) {
            throw new InvalidTransitionException("Chỉ có thể hủy phiên BOT hoặc đang chờ");
        }
        session.setStatus(ChatSessionStatus.ENDED);
        session.setEndReason("STUDENT_CANCEL");
        session.setEndedBy(studentId);
        session.setEndedAt(LocalDateTime.now());
        sessions.saveAndFlush(session);
        events.publishEvent(new ChatQueueChangedEvent(session.getCategoryId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChatSessionView> current(Long studentId) {
        return sessions.findFirstByStudentIdAndStatusInOrderByIdDesc(studentId, CURRENT_STATUSES)
                .map(session -> toView(session, null));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageView> messages(Long sessionId, Long userId, Long afterId) {
        ChatSession session = sessions.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên chat"));
        if (!Objects.equals(session.getStudentId(), userId)
                && !Objects.equals(session.getManagerId(), userId)) {
            throw new NotFoundException("Không tìm thấy phiên chat");
        }
        List<ChatMessage> page = messages.findTop200BySessionIdAndIdGreaterThanOrderByIdAsc(
                sessionId, afterId == null ? 0L : Math.max(0L, afterId));
        Set<Long> senderIds = page.stream().map(ChatMessage::getSenderId)
                .filter(Objects::nonNull).collect(java.util.stream.Collectors.toSet());
        Map<Long, UserSummary> summaries = senderIds.isEmpty() ? Map.of() : users.getSummaries(senderIds);
        return page.stream().map(item -> new ChatMessageView(item.getId(), item.getSessionId(),
                item.getSenderType(), item.getSenderId(), senderName(item, summaries), item.getContent(),
                item.getContentType(), item.getAssetId(), item.getClientMessageId(),
                item.getSentAt(), item.getReadAt())).toList();
    }

    @Override
    public void rate(Long sessionId, Long studentId, int rating, String comment) {
        ChatSession session = requireOwn(sessionId, studentId);
        if (session.getStatus() != ChatSessionStatus.ENDED) {
            throw new InvalidTransitionException("Chỉ có thể đánh giá phiên đã kết thúc");
        }
        if (rating < 1 || rating > 5 || comment != null && comment.trim().length() > 500) {
            throw new BadRequestException("Điểm phải từ 1–5 và nhận xét tối đa 500 ký tự");
        }
        session.setRating(rating);
        session.setRatingComment(comment == null || comment.isBlank() ? null : comment.trim());
        sessions.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatHistoryItem> history(Long studentId, Pageable p) {
        return sessions.findByStudentIdOrderByIdDesc(studentId, p).map(session -> {
            UserSummary manager = session.getManagerId() == null ? null : users.getSummary(session.getManagerId());
            String categoryName = categories.findCategory(session.getCategoryId()).map(CategoryView::name).orElse("");
            return new ChatHistoryItem(session.getId(), session.getStartedAt(), session.getEndedAt(),
                    manager == null ? null : manager.fullName(), categoryName, session.getStatus());
        });
    }

    @Override
    public Long createTicketFromChat(Long sessionId, Long userId) {
        ChatSession session = sessions.findLockedById(sessionId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên chat"));
        if (!Objects.equals(userId, session.getStudentId())
                && !Objects.equals(userId, session.getManagerId())) {
            throw new NotFoundException("Không tìm thấy phiên chat");
        }
        if (session.getTicketId() != null) {
            throw new InvalidTransitionException("Phiên chat đã được chuyển thành yêu cầu tư vấn");
        }
        String question = session.getInitialQuestion();
        String title = "Từ hội thoại chat: " + question.substring(0, Math.min(100, question.length()));
        String description = transcript(sessionId);
        CreateTicketCommand command = new CreateTicketCommand(session.getCategoryId(), title,
                description, false, null, false, UUID.randomUUID().toString(), List.of());
        Long ticketId = ticketWorkflow.createTicket(session.getStudentId(), command).getId();
        session.setTicketId(ticketId);
        sessions.saveAndFlush(session);
        return ticketId;
    }

    private String transcript(Long sessionId) {
        StringBuilder html = new StringBuilder("<p>Biên bản hội thoại tư vấn trực tuyến QAUTE</p>");
        int textLength = 40;
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        for (ChatMessage message : messages.findBySessionIdOrderByIdAsc(sessionId)) {
            String name = message.getSenderId() == null
                    ? message.getSenderType() == ChatSenderType.BOT ? "Trợ lý QAUTE" : "Hệ thống"
                    : getSenderName(message.getSenderId());
            String line = "[" + message.getSentAt().format(timeFormat) + "] " + name + ": "
                    + message.getContent().replace('\n', ' ').replace('\r', ' ');
            int remaining = 4800 - textLength;
            if (remaining <= 0) break;
            if (line.length() > remaining) line = line.substring(0, remaining);
            html.append("<p>").append(HtmlUtils.htmlEscape(line)).append("</p>");
            textLength += line.length();
        }
        return html.toString();
    }

    private String getSenderName(Long senderId) {
        UserSummary sender = users.getSummary(senderId);
        return sender == null ? "Người tham gia" : sender.fullName();
    }

    private ChatSession requireOwn(Long id, Long studentId) {
        return sessions.findById(id).filter(session -> Objects.equals(session.getStudentId(), studentId))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên chat"));
    }

    private ChatSessionView toView(ChatSession session, List<FaqSuggestion> suggestions) {
        return toView(session, suggestions, availability.isWorkingNow(), null);
    }

    private ChatSessionView toView(ChatSession session, List<FaqSuggestion> suggestions,
            boolean working, String message) {
        Integer queuePosition = null;
        if (session.getStatus() == ChatSessionStatus.WAITING) {
            queuePosition = Math.toIntExact(sessions.queuePosition(ChatSessionStatus.WAITING,
                    session.getCategoryId(), session.getStartedAt(), session.getId()));
        }
        UserSummary manager = session.getManagerId() == null ? null : users.getSummary(session.getManagerId());
        List<FaqSuggestion> resultSuggestions = suggestions != null ? suggestions
                : session.getStatus() == ChatSessionStatus.BOT ? faqs.suggest(session.getInitialQuestion(), 3) : List.of();
        return new ChatSessionView(session.getId(), session.getStatus(), session.getCategoryId(),
                categories.getDisplayPath(session.getCategoryId()), session.getInitialQuestion(),
                session.getManagerId(), manager == null ? null : manager.fullName(), queuePosition,
                working, message, resultSuggestions, session.getStartedAt());
    }

    private String senderName(ChatMessage item, Map<Long, UserSummary> summaries) {
        if (item.getSenderId() != null && summaries.containsKey(item.getSenderId())) {
            return summaries.get(item.getSenderId()).fullName();
        }
        return item.getSenderType() == ChatSenderType.BOT ? "Trợ lý QAUTE" : "Hệ thống";
    }

    private String botContent(List<FaqSuggestion> suggestions) {
        if (suggestions == null || suggestions.isEmpty()) {
            return "Chưa tìm thấy FAQ phù hợp. Bạn có thể yêu cầu tư vấn viên hỗ trợ.";
        }
        StringBuilder content = new StringBuilder("Một số FAQ có thể giúp bạn:\n");
        for (FaqSuggestion suggestion : suggestions) {
            content.append("• ").append(suggestion.title()).append(" (/faq/")
                    .append(suggestion.slug()).append(")\n");
        }
        content.append("Bạn có thể yêu cầu tư vấn viên nếu vẫn cần hỗ trợ.");
        return content.length() > 2000 ? content.substring(0, 2000) : content.toString();
    }

    private String trimRequired(String value, int min, int max, String error) {
        if (value == null || value.trim().length() < min || value.trim().length() > max) {
            throw new BadRequestException(error);
        }
        return value.trim();
    }
}
