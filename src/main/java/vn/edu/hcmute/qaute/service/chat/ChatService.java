package vn.edu.hcmute.qaute.service.chat;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.dto.view.ChatHistoryItem;
import vn.edu.hcmute.qaute.dto.view.ChatConversationHistoryItem;
import vn.edu.hcmute.qaute.dto.view.ChatMessageView;
import vn.edu.hcmute.qaute.dto.view.ChatSessionView;

// Xử lý nghiệp vụ Chat trong hệ thống.
public interface ChatService {
    // Bắt đầu phiên chat và trả về thông tin phiên.
    ChatSessionView start(Long studentId, Long categoryId, String question);
    // Yêu cầu tư vấn viên tiếp nhận phiên chat.
    ChatSessionView requestAgent(Long sessionId, Long studentId);
    // Hủy phiên chat của sinh viên.
    void cancel(Long sessionId, Long studentId);
    // Tìm phiên chat hiện tại của sinh viên nếu có.
    Optional<ChatSessionView> current(Long studentId);
    // Lấy tin nhắn của phiên chat sau mã tin nhắn đã chỉ định.
    List<ChatMessageView> messages(Long sessionId, Long userId, Long afterId);
    // Lưu đánh giá của sinh viên cho phiên chat đã kết thúc.
    void rate(Long sessionId, Long studentId, int rating, String comment);
    // Lấy lịch sử phiên chat của sinh viên theo trang.
    Page<ChatHistoryItem> history(Long studentId, Pageable p);
    Page<ChatConversationHistoryItem> managerHistory(Long managerId, Pageable p);
    Page<ChatConversationHistoryItem> adminHistory(Pageable p);
    // Tạo yêu cầu tư vấn từ nội dung phiên chat.
    Long createTicketFromChat(Long sessionId, Long userId);
}
