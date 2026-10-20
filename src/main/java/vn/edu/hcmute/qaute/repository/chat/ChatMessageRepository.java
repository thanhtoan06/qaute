package vn.edu.hcmute.qaute.repository.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.chat.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySessionIdAndIdGreaterThanOrderByIdAsc(Long sessionId, Long afterId);

    List<ChatMessage> findTop200BySessionIdAndIdGreaterThanOrderByIdAsc(Long sessionId, Long afterId);

    Optional<ChatMessage> findBySessionIdAndClientMessageId(Long sessionId, String clientMessageId);

    @Modifying
    @Query("update ChatMessage m set m.readAt = :now where m.sessionId = :sessionId "
            + "and (m.senderId is null or m.senderId <> :readerId) "
            + "and m.id <= :lastId and m.readAt is null")
    int markRead(@Param("sessionId") Long sessionId, @Param("readerId") Long readerId,
            @Param("lastId") Long lastId, @Param("now") LocalDateTime now);
}
