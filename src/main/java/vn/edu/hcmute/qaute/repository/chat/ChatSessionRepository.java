package vn.edu.hcmute.qaute.repository.chat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ChatSession s where s.id = :id")
    Optional<ChatSession> findLockedById(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ChatSession s set s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.ACTIVE, "
            + "s.managerId = :managerId, s.acceptedAt = :now, s.lastMessageAt = :now, s.version = s.version + 1 "
            + "where s.id = :id and s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.WAITING")
    int acceptWaiting(@Param("id") Long id, @Param("managerId") Long managerId,
            @Param("now") LocalDateTime now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ChatSession s set s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.EXPIRED, "
            + "s.endReason = 'TIMEOUT', s.endedAt = :now, s.version = s.version + 1 "
            + "where s.id = :id and s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.WAITING "
            + "and coalesce(s.lastMessageAt, s.startedAt) < :cutoff")
    int expireWaiting(@Param("id") Long id, @Param("cutoff") LocalDateTime cutoff,
            @Param("now") LocalDateTime now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ChatSession s set s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.ENDED, "
            + "s.endReason = 'IDLE', s.endedAt = :now, s.version = s.version + 1 "
            + "where s.id = :id and s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.ACTIVE "
            + "and coalesce(s.lastMessageAt, s.acceptedAt) < :cutoff")
    int endIdle(@Param("id") Long id, @Param("cutoff") LocalDateTime cutoff,
            @Param("now") LocalDateTime now);
    Optional<ChatSession> findFirstByStudentIdAndStatusInOrderByIdDesc(Long studentId,
            Collection<ChatSessionStatus> statuses);

    List<ChatSession> findByStatusOrderByStartedAtAsc(ChatSessionStatus status);

    List<ChatSession> findByStatusAndLastMessageAtBeforeOrderByLastMessageAtAsc(
            ChatSessionStatus status, LocalDateTime cutoff);

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
