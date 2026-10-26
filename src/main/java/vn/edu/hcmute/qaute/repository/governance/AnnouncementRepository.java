package vn.edu.hcmute.qaute.repository.governance;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.AnnouncementStatus;
import vn.edu.hcmute.qaute.entity.governance.Announcement;

// Khai báo truy vấn lưu trữ dữ liệu Announcement.
public interface AnnouncementRepository extends JpaRepository<Announcement, Long>, JpaSpecificationExecutor<Announcement> {
    // Kiểm tra thông báo đã tồn tại theo tiêu đề.
    boolean existsByTitle(String title);

    // Tìm thông báo đến thời điểm xuất bản.
    List<Announcement> findByStatusAndPublishAtLessThanEqual(AnnouncementStatus status, LocalDateTime now);

    // Tìm thông báo đã hết hạn.
    List<Announcement> findByStatusAndExpiresAtLessThanEqual(AnnouncementStatus status, LocalDateTime now);

    // Tìm thông báo theo từ khóa trong tiêu đề hoặc tóm tắt.
    @Query("SELECT a FROM Announcement a WHERE a.status = :status " +
           "AND a.publishAt <= :now " +
           "AND (a.expiresAt IS NULL OR a.expiresAt > :now) " +
           "AND (LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(a.summary) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Announcement> searchByQuery(@Param("query") String query,
                                   @Param("status") AnnouncementStatus status,
                                   @Param("now") LocalDateTime now);
}
