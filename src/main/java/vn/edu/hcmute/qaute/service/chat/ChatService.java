package vn.edu.hcmute.qaute.service.chat;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.dto.view.ChatHistoryItem;
import vn.edu.hcmute.qaute.dto.view.ChatMessageView;
import vn.edu.hcmute.qaute.dto.view.ChatSessionView;

public interface ChatService {
    ChatSessionView start(Long studentId, Long categoryId, String question);
    ChatSessionView requestAgent(Long sessionId, Long studentId);
    void cancel(Long sessionId, Long studentId);
    Optional<ChatSessionView> current(Long studentId);
    List<ChatMessageView> messages(Long sessionId, Long userId, Long afterId);
    void rate(Long sessionId, Long studentId, int rating, String comment);
    Page<ChatHistoryItem> history(Long studentId, Pageable p);
    Long createTicketFromChat(Long sessionId, Long userId);
}
