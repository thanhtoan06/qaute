package vn.edu.hcmute.qaute.service.governance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.dto.view.AnnouncementView;

// Xử lý nghiệp vụ AnnouncementQuery trong hệ thống.
public interface AnnouncementQueryService {
    // Tìm danh sách thông báo đang hoạt động cho đối tượng nhận.
    List<AnnouncementView> findActiveFor(AnnouncementAudience audience, int limit);

    // Tìm trang thông báo đang hoạt động cho đối tượng nhận.
    Page<AnnouncementView> findActivePage(AnnouncementAudience audience, int page, int size);

    // Tìm thông báo đã đăng còn hiển thị theo đối tượng nhận.
    Optional<AnnouncementView> findPublished(Long id, AnnouncementAudience audience);

    // Tìm thông báo theo từ khóa.
    List<AnnouncementView> searchByQuery(String query, AnnouncementAudience audience);
}
