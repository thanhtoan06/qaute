package vn.edu.hcmute.qaute.repository.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.chat.ChatMessage;

// Khai báo truy vấn lưu trữ dữ liệu ChatMessage.
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // Tìm tin nhắn mới hơn ID đã nhận.
    List<ChatMessage> findBySessionIdAndIdGreaterThanOrderByIdAsc(Long sessionId, Long afterId);

    // Lấy tối đa 200 tin nhắn mới hơn ID đã nhận.
    List<ChatMessage> findTop200BySessionIdAndIdGreaterThanOrderByIdAsc(Long sessionId, Long afterId);

    // Lấy tối đa 100 tin nhắn mới hơn ID đã nhận.
    List<ChatMessage> findTop100BySessionIdAndIdGreaterThanOrderByIdAsc(Long sessionId, Long afterId);

    // Liệt kê tin nhắn theo thứ tự tăng dần.
    List<ChatMessage> findBySessionIdOrderByIdAsc(Long sessionId);

    // Tìm tin nhắn theo mã phía máy khách.
    Optional<ChatMessage> findBySessionIdAndClientMessageId(Long sessionId, String clientMessageId);

    // Đánh dấu tin nhắn đến là đã đọc.
    @Modifying
    @Query("update ChatMessage m set m.readAt = :now where m.sessionId = :sessionId "
            + "and (m.senderId is null or m.senderId <> :readerId) "
            + "and m.id <= :lastId and m.readAt is null")
    int markRead(@Param("sessionId") Long sessionId, @Param("readerId") Long readerId,
            @Param("lastId") Long lastId, @Param("now") LocalDateTime now);
}
