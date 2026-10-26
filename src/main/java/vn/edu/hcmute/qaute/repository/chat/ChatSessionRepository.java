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

// Khai báo truy vấn lưu trữ dữ liệu ChatSession.
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    // Kiểm tra câu hỏi khởi tạo đã tồn tại.
    boolean existsByStudentIdAndInitialQuestion(Long studentId, String initialQuestion);

    // Tìm phiên trò chuyện và khóa bản ghi.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ChatSession s where s.id = :id")
    Optional<ChatSession> findLockedById(@Param("id") Long id);

    // Chấp nhận phiên đang chờ.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ChatSession s set s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.ACTIVE, "
            + "s.managerId = :managerId, s.acceptedAt = :now, s.lastMessageAt = :now, s.version = s.version + 1 "
            + "where s.id = :id and s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.WAITING")
    int acceptWaiting(@Param("id") Long id, @Param("managerId") Long managerId,
            @Param("now") LocalDateTime now);

    // Đánh dấu phiên chờ đã hết hạn.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ChatSession s set s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.EXPIRED, "
            + "s.endReason = 'TIMEOUT', s.endedAt = :now, s.version = s.version + 1 "
            + "where s.id = :id and s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.WAITING "
            + "and coalesce(s.lastMessageAt, s.startedAt) < :cutoff")
    int expireWaiting(@Param("id") Long id, @Param("cutoff") LocalDateTime cutoff,
            @Param("now") LocalDateTime now);

    // Kết thúc phiên không hoạt động.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ChatSession s set s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.ENDED, "
            + "s.endReason = 'IDLE', s.endedAt = :now, s.version = s.version + 1 "
            + "where s.id = :id and s.status = vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.ACTIVE "
            + "and coalesce(s.lastMessageAt, s.acceptedAt) < :cutoff")
    int endIdle(@Param("id") Long id, @Param("cutoff") LocalDateTime cutoff,
            @Param("now") LocalDateTime now);

    // Tìm phiên gần nhất theo trạng thái.
    Optional<ChatSession> findFirstByStudentIdAndStatusInOrderByIdDesc(Long studentId,
            Collection<ChatSessionStatus> statuses);

    // Liệt kê phiên theo trạng thái và thời điểm bắt đầu.
    List<ChatSession> findByStatusOrderByStartedAtAsc(ChatSessionStatus status);

    // Phân trang phiên theo danh mục.
    Page<ChatSession> findByStatusAndCategoryIdInOrderByStartedAtAscIdAsc(
            ChatSessionStatus status, Collection<Long> categoryIds, Pageable pageable);

    // Đếm phiên theo trạng thái và danh mục.
    long countByStatusAndCategoryIdIn(ChatSessionStatus status, Collection<Long> categoryIds);

    // Tìm phiên có tin nhắn cuối quá hạn.
    List<ChatSession> findByStatusAndLastMessageAtBeforeOrderByLastMessageAtAsc(
            ChatSessionStatus status, LocalDateTime cutoff);

    // Tìm phiên của người quản lý theo trạng thái.
    List<ChatSession> findByManagerIdAndStatus(Long managerId, ChatSessionStatus status);

    // Đếm phiên của người quản lý theo trạng thái.
    long countByManagerIdAndStatus(Long managerId, ChatSessionStatus status);

    // Phân trang phiên của sinh viên.
    Page<ChatSession> findByStudentIdOrderByIdDesc(Long studentId, Pageable pageable);

    Page<ChatSession> findByManagerIdOrderByIdDesc(Long managerId, Pageable pageable);

    Page<ChatSession> findAllByOrderByIdDesc(Pageable pageable);

    // Đếm phiên bắt đầu trước thời điểm chỉ định.
    long countByStatusAndCategoryIdInAndStartedAtBefore(ChatSessionStatus status,
            Collection<Long> categoryIds, LocalDateTime startedAt);

    // Tính vị trí trong hàng đợi.
    @Query("select count(s) from ChatSession s where s.status = :status and s.categoryId = :categoryId "
            + "and (s.startedAt < :startedAt or (s.startedAt = :startedAt and s.id <= :id))")
    long queuePosition(@Param("status") ChatSessionStatus status, @Param("categoryId") Long categoryId,
            @Param("startedAt") LocalDateTime startedAt, @Param("id") Long id);
}
