package vn.edu.hcmute.qaute.service.chat;

import vn.edu.hcmute.qaute.common.util.BoundedPages;

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
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.dto.view.ChatHistoryItem;
import vn.edu.hcmute.qaute.dto.view.ChatConversationHistoryItem;
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
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;
import vn.edu.hcmute.qaute.service.ticket.command.CreateTicketCommand;

// Xử lý nghiệp vụ Chat trong hệ thống.
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
    private final AuditService audit;

    // Khởi tạo dịch vụ chat cùng các dịch vụ nghiệp vụ liên quan.
    public ChatServiceImpl(ChatSessionRepository sessions, ChatMessageRepository messages,
            ChatAvailabilityService availability, CategoryQueryService categories, FaqQueryService faqs,
            UserLookupService users, NotificationService notifications,
            ApplicationEventPublisher events, EntityManager entityManager,
            TicketWorkflowService ticketWorkflow, AuditService audit) {
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
        this.audit = audit;
    }

    @Override
    // Tạo phiên BOT mới hoặc trả về phiên hiện tại của sinh viên.
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
        audit.log(AuditActions.CHAT_SESSION_STARTED, "ChatSession", session.getId(),
                null, "status=BOT; categoryId=" + categoryId);

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
    // Chuyển phiên BOT sang hàng đợi tư vấn viên khi có người đủ điều kiện.
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
            audit.log(AuditActions.CHAT_SESSION_EXPIRED, "ChatSession", session.getId(),
                    "status=BOT", "status=EXPIRED; reason=NO_AGENT");
            return toView(session, List.of(), working,
                    "Hiện chưa có tư vấn viên trực. Bạn có thể chuyển thành yêu cầu tư vấn.");
        }
        session.setStatus(ChatSessionStatus.WAITING);
        session.setLastMessageAt(LocalDateTime.now());
        sessions.saveAndFlush(session);
        audit.log(AuditActions.CHAT_AGENT_REQUESTED, "ChatSession", session.getId(),
                "status=BOT", "status=WAITING");
        events.publishEvent(new ChatQueueChangedEvent(session.getCategoryId()));
        notifications.notifyUsers(eligible, NotificationType.CHAT_WAITING_NEW,
                "Sinh viên đang chờ tư vấn", session.getInitialQuestion(),
                "/manager/chat", "CHAT_SESSION", session.getId());
        return toView(session, List.of(), true, "Đang chờ tư vấn viên tiếp nhận.");
    }

    @Override
    // Hủy phiên BOT hoặc phiên đang chờ của sinh viên.
    public void cancel(Long sessionId, Long studentId) {
        ChatSession session = requireOwn(sessionId, studentId);
        if (session.getStatus() != ChatSessionStatus.BOT && session.getStatus() != ChatSessionStatus.WAITING) {
            throw new InvalidTransitionException("Chỉ có thể hủy phiên BOT hoặc đang chờ");
        }
        ChatSessionStatus previous = session.getStatus();
        session.setStatus(ChatSessionStatus.ENDED);
        session.setEndReason("STUDENT_CANCEL");
        session.setEndedBy(studentId);
        session.setEndedAt(LocalDateTime.now());
        sessions.saveAndFlush(session);
        audit.log(AuditActions.CHAT_SESSION_CANCELLED, "ChatSession", session.getId(),
                "status=" + previous, "status=ENDED; reason=STUDENT_CANCEL");
        events.publishEvent(new ChatQueueChangedEvent(session.getCategoryId()));
    }

    @Override
    @Transactional(readOnly = true)
    // Tìm phiên chat đang hoạt động gần nhất của sinh viên.
    public Optional<ChatSessionView> current(Long studentId) {
        return sessions.findFirstByStudentIdAndStatusInOrderByIdDesc(studentId, CURRENT_STATUSES)
                .map(session -> toView(session, null));
    }

    @Override
    @Transactional(readOnly = true)
    // Lấy tin nhắn mới của phiên chat sau khi xác thực người tham gia.
    public List<ChatMessageView> messages(Long sessionId, Long userId, Long afterId) {
        ChatSession session = sessions.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên chat"));
        if (userId == null || (!Objects.equals(session.getStudentId(), userId)
                && !Objects.equals(session.getManagerId(), userId)
                && !vn.edu.hcmute.qaute.security.SecurityUtils.hasRole(RoleCode.ADMIN))) {
            throw new NotFoundException("Không tìm thấy phiên chat");
        }
        long cursor = afterId == null ? 0L : Math.max(0L, afterId);
        // Keep the TV1 student contract; the Manager desk paginates at 100 messages.
        List<ChatMessage> page = vn.edu.hcmute.qaute.security.SecurityUtils.hasRole(
                vn.edu.hcmute.qaute.common.constant.RoleCode.MANAGER)
                ? messages.findTop100BySessionIdAndIdGreaterThanOrderByIdAsc(sessionId, cursor)
                : messages.findTop200BySessionIdAndIdGreaterThanOrderByIdAsc(sessionId, cursor);
        Set<Long> senderIds = page.stream().map(ChatMessage::getSenderId)
                .filter(Objects::nonNull).collect(java.util.stream.Collectors.toSet());
        Map<Long, UserSummary> summaries = senderIds.isEmpty() ? Map.of() : users.getSummaries(senderIds);
        return page.stream().map(item -> new ChatMessageView(item.getId(), item.getSessionId(),
                item.getSenderType(), item.getSenderId(), senderName(item, summaries), item.getContent(),
                item.getContentType(), item.getAssetId(), item.getClientMessageId(),
                item.getSentAt(), item.getReadAt())).toList();
    }

    @Override
    // Lưu điểm và nhận xét đánh giá cho phiên chat đã kết thúc.
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
    // Lấy lịch sử phiên chat của sinh viên theo trang yêu cầu.
    public Page<ChatHistoryItem> history(Long studentId, Pageable p) {
        return sessions.findByStudentIdOrderByIdDesc(studentId, BoundedPages.of(p)).map(session -> {
            UserSummary manager = session.getManagerId() == null ? null : users.getSummary(session.getManagerId());
            String categoryName = categories.findCategory(session.getCategoryId()).map(CategoryView::name).orElse("");
            return new ChatHistoryItem(session.getId(), session.getStartedAt(), session.getEndedAt(),
                    manager == null ? null : manager.fullName(), categoryName, session.getStatus());
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatConversationHistoryItem> managerHistory(Long managerId, Pageable p) {
        return sessions.findByManagerIdOrderByIdDesc(managerId, BoundedPages.of(p))
                .map(this::toConversationHistoryItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatConversationHistoryItem> adminHistory(Pageable p) {
        return sessions.findAllByOrderByIdDesc(BoundedPages.of(p))
                .map(this::toConversationHistoryItem);
    }

    private ChatConversationHistoryItem toConversationHistoryItem(ChatSession session) {
        UserSummary student = users.getSummary(session.getStudentId());
        UserSummary manager = session.getManagerId() == null ? null : users.getSummary(session.getManagerId());
        String categoryName = categories.findCategory(session.getCategoryId())
                .map(CategoryView::name).orElse("");
        return new ChatConversationHistoryItem(session.getId(), session.getStartedAt(), session.getEndedAt(),
                student == null ? "Sinh viên" : student.fullName(),
                manager == null ? null : manager.fullName(), categoryName,
                session.getInitialQuestion(), session.getStatus());
    }

    @Override
    // Tạo yêu cầu tư vấn từ phiên chat và liên kết mã yêu cầu với phiên.
    public Long createTicketFromChat(Long sessionId, Long userId) {
        ChatSession session = sessions.findLockedById(sessionId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên chat"));
        if (userId == null || (!Objects.equals(userId, session.getStudentId())
                && !Objects.equals(userId, session.getManagerId()))) {
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

    // Tạo bản ghi hội thoại HTML đã giới hạn độ dài để đưa vào yêu cầu tư vấn.
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

    // Lấy tên hiển thị của người gửi hoặc giá trị dự phòng.
    private String getSenderName(Long senderId) {
        UserSummary sender = users.getSummary(senderId);
        return sender == null ? "Người tham gia" : sender.fullName();
    }

    // Tìm phiên chat thuộc về sinh viên đã chỉ định.
    private ChatSession requireOwn(Long id, Long studentId) {
        return sessions.findById(id).filter(session -> Objects.equals(session.getStudentId(), studentId))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên chat"));
    }

    // Tạo dữ liệu hiển thị phiên chat cùng vị trí hàng đợi và FAQ gợi ý.
    private ChatSessionView toView(ChatSession session, List<FaqSuggestion> suggestions) {
        return toView(session, suggestions, availability.isWorkingNow(), null);
    }

    // Tạo dữ liệu hiển thị phiên chat với trạng thái giờ làm việc và thông báo tùy chỉnh.
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

    // Chọn tên hiển thị phù hợp cho người gửi tin nhắn.
    private String senderName(ChatMessage item, Map<Long, UserSummary> summaries) {
        if (item.getSenderId() != null && summaries.containsKey(item.getSenderId())) {
            return summaries.get(item.getSenderId()).fullName();
        }
        return item.getSenderType() == ChatSenderType.BOT ? "Trợ lý QAUTE" : "Hệ thống";
    }

    // Tạo nội dung phản hồi BOT từ các FAQ gợi ý.
    private String botContent(List<FaqSuggestion> suggestions) {
        if (suggestions == null || suggestions.isEmpty()) {
            return "Chưa tìm thấy FAQ phù hợp. Bạn có thể yêu cầu tư vấn viên hỗ trợ.";
        }

        // Nếu có FAQ phù hợp, trả lời FAQ đầu tiên với toàn bộ nội dung
        FaqSuggestion bestMatch = suggestions.get(0);
        StringBuilder content = new StringBuilder();

        // Chỉ hiển thị toàn bộ nội dung FAQ (bodyHtml)
        if (bestMatch.bodyHtml() != null && !bestMatch.bodyHtml().isBlank()) {
            // Loại bỏ thẻ HTML để hiển thị dạng text trong chat
            String cleanContent = stripHtml(bestMatch.bodyHtml());
            content.append(cleanContent);
        } else if (bestMatch.summary() != null && !bestMatch.summary().isBlank()) {
            // Fallback: dùng summary nếu không có bodyHtml
            content.append(bestMatch.summary());
        }

        content.append("\n\n---\nXem chi tiết: /faq/").append(bestMatch.slug());

        // Nếu có nhiều FAQ phù hợp, liệt kê thêm
        if (suggestions.size() > 1) {
            content.append("\n\nCác câu hỏi liên quan khác:\n");
            for (int i = 1; i < suggestions.size(); i++) {
                FaqSuggestion suggestion = suggestions.get(i);
                content.append("• ").append(suggestion.title()).append(" (/faq/")
                        .append(suggestion.slug()).append(")\n");
            }
        }

        content.append("\n\nBạn có thể yêu cầu tư vấn viên nếu vẫn cần hỗ trợ.");

        // Giới hạn độ dài tin nhắn (tối đa 5000 ký tự)
        return content.length() > 5000 ? content.substring(0, 5000) + "..." : content.toString();
    }

    // Loại bỏ thẻ HTML để lấy nội dung text thuần
    private String stripHtml(String html) {
        if (html == null) return "";
        // Loại bỏ thẻ HTML cơ bản
        String text = html.replaceAll("<[^>]+>", " ");
        // Thay thế các ký tự đặc biệt HTML
        text = text.replace("&nbsp;", " ")
                  .replace("&amp;", "&")
                  .replace("&lt;", "<")
                  .replace("&gt;", ">")
                  .replace("&quot;", "\"")
                  .replace("&#39;", "'");
        // Xóa khoảng trắng thừa
        text = text.replaceAll("\\s+", " ").trim();
        return text;
    }

    // Chuẩn hóa và kiểm tra độ dài của chuỗi bắt buộc.
    private String trimRequired(String value, int min, int max, String error) {
        if (value == null || value.trim().length() < min || value.trim().length() > max) {
            throw new BadRequestException(error);
        }
        return value.trim();
    }
}
