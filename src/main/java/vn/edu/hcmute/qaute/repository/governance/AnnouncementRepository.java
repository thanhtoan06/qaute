package vn.edu.hcmute.qaute.repository.governance;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.edu.hcmute.qaute.common.constant.AnnouncementStatus;
import vn.edu.hcmute.qaute.entity.governance.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long>, JpaSpecificationExecutor<Announcement> {
    boolean existsByTitle(String title);

    List<Announcement> findByStatusAndPublishAtLessThanEqual(AnnouncementStatus status, LocalDateTime now);
    List<Announcement> findByStatusAndExpiresAtLessThanEqual(AnnouncementStatus status, LocalDateTime now);
}
