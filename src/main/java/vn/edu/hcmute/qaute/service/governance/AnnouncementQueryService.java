package vn.edu.hcmute.qaute.service.governance;

import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.dto.view.AnnouncementView;

import org.springframework.data.domain.Page;

public interface AnnouncementQueryService {
    List<AnnouncementView> findActiveFor(AnnouncementAudience audience, int limit);
    Page<AnnouncementView> findActivePage(AnnouncementAudience audience, int page, int size);
    Optional<AnnouncementView> findPublished(Long id, AnnouncementAudience audience);
}
