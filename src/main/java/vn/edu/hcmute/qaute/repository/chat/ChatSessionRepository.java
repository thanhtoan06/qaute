package vn.edu.hcmute.qaute.repository.chat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    Optional<ChatSession> findFirstByStudentIdAndStatusInOrderByIdDesc(Long studentId,
            Collection<ChatSessionStatus> statuses);

    List<ChatSession> findByStatusOrderByStartedAtAsc(ChatSessionStatus status);

    List<ChatSession> findByManagerIdAndStatus(Long managerId, ChatSessionStatus status);

    long countByManagerIdAndStatus(Long managerId, ChatSessionStatus status);

    Page<ChatSession> findByStudentIdOrderByIdDesc(Long studentId, Pageable pageable);

    long countByStatusAndCategoryIdInAndStartedAtBefore(ChatSessionStatus status,
            Collection<Long> categoryIds, LocalDateTime startedAt);

    @Query("select count(s) from ChatSession s where s.status = :status and s.categoryId = :categoryId "
            + "and (s.startedAt < :startedAt or (s.startedAt = :startedAt and s.id <= :id))")
    long queuePosition(@Param("status") ChatSessionStatus status, @Param("categoryId") Long categoryId,
            @Param("startedAt") LocalDateTime startedAt, @Param("id") Long id);
}
