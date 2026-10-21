package vn.edu.hcmute.qaute.service.governance;

import vn.edu.hcmute.qaute.common.util.HtmlSanitizerUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.common.constant.AnnouncementStatus;
import vn.edu.hcmute.qaute.dto.view.AnnouncementView;
import vn.edu.hcmute.qaute.entity.governance.Announcement;
import vn.edu.hcmute.qaute.repository.governance.AnnouncementRepository;
import vn.edu.hcmute.qaute.service.media.MediaService;

@Service
@Transactional(readOnly = true)
public class AnnouncementQueryServiceImpl implements AnnouncementQueryService {
    private final AnnouncementRepository announcements;
    private final MediaService media;

    public AnnouncementQueryServiceImpl(AnnouncementRepository announcements, MediaService media) {
        this.announcements = announcements;
        this.media = media;
    }

    @Override
    public List<AnnouncementView> findActiveFor(AnnouncementAudience audience, int limit) {
        if (limit <= 0) return List.of();
        return announcements.findAll(active(audience, LocalDateTime.now()), PageRequest.of(0, Math.min(limit, 100),
                Sort.by(Sort.Order.desc("pinned"), Sort.Order.desc("publishAt"), Sort.Order.desc("id"))))
                .map(this::view).getContent();
    }

    @Override
    public Optional<AnnouncementView> findPublished(Long id, AnnouncementAudience audience) {
        return announcements.findOne(active(audience, LocalDateTime.now()).and((root, query, cb) -> cb.equal(root.get("id"), id)))
                .map(this::view);
    }

    private Specification<Announcement> active(AnnouncementAudience audience, LocalDateTime now) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), AnnouncementStatus.PUBLISHED),
                cb.lessThanOrEqualTo(root.get("publishAt"), now),
                cb.or(cb.isNull(root.get("expiresAt")), cb.greaterThan(root.get("expiresAt"), now)),
                audience == AnnouncementAudience.ALL ? cb.equal(root.get("audience"), AnnouncementAudience.ALL)
                        : cb.or(cb.equal(root.get("audience"), AnnouncementAudience.ALL), cb.equal(root.get("audience"), audience)));
    }

    private AnnouncementView view(Announcement a) {
        return new AnnouncementView(a.getId(), a.getTitle(), a.getSummary(), HtmlSanitizerUtil.sanitize(a.getBodyHtml()),
                a.getCoverAssetId() == null ? null : media.publicUrl(a.getCoverAssetId()),
                a.isPinned(), a.getPublishAt());
    }
}
