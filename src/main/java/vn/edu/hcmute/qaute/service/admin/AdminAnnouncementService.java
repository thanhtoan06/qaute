package vn.edu.hcmute.qaute.service.admin;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AnnouncementStatus;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.HtmlSanitizerUtil;
import vn.edu.hcmute.qaute.common.util.BoundedPages;
import vn.edu.hcmute.qaute.dto.request.admin.AnnouncementForm;
import vn.edu.hcmute.qaute.entity.governance.Announcement;
import vn.edu.hcmute.qaute.repository.governance.AnnouncementRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.media.MediaService;

@Service
@Transactional(readOnly = true)
public class AdminAnnouncementService {
    private final AnnouncementRepository announcements;
    private final MediaService media;
    private final AuditService audit;

    public AdminAnnouncementService(AnnouncementRepository announcements, MediaService media, AuditService audit) {
        this.announcements = announcements;
        this.media = media;
        this.audit = audit;
    }

    public Page<Announcement> list(AnnouncementStatus status, Pageable pageable) {
        Specification<Announcement> filter = (root, query, cb) -> status == null
                ? cb.conjunction() : cb.equal(root.get("status"), status);
        return announcements.findAll(filter, BoundedPages.of(pageable));
    }

    public AnnouncementForm getForm(Long id) {
        Announcement a = get(id);
        AnnouncementForm f = new AnnouncementForm();
        f.setId(a.getId()); f.setTitle(a.getTitle()); f.setSummary(a.getSummary());
        f.setBodyHtml(a.getBodyHtml()); f.setAudience(a.getAudience()); f.setPinned(a.isPinned());
        f.setPublishAt(a.getPublishAt()); f.setExpiresAt(a.getExpiresAt());
        f.setCoverAssetId(a.getCoverAssetId()); f.setNotifyOnPublish(a.isNotifyOnPublish());
        f.setAutoPublish(a.isAutoPublish());
        return f;
    }

    @Transactional
    public Long save(AnnouncementForm form, Long actorId) {
        if (form.getTitle() == null || form.getTitle().strip().length() < 5 || form.getTitle().length() > 200
                || form.getSummary() == null || form.getSummary().strip().length() < 10
                || form.getSummary().length() > 500 || form.getAudience() == null) {
            throw new BadRequestException("Thông tin thông báo chưa hợp lệ");
        }
        String body = HtmlSanitizerUtil.sanitize(form.getBodyHtml());
        if (body.isBlank()) throw new BadRequestException("Nội dung thông báo không được để trống");
        LocalDateTime effectivePublish = form.getPublishAt() == null ? LocalDateTime.now() : form.getPublishAt();
        if (form.getExpiresAt() != null && !form.getExpiresAt().isAfter(effectivePublish)) {
            throw new BadRequestException("Thời gian hết hạn phải sau thời gian đăng");
        }
        if (form.isAutoPublish() && (form.getPublishAt() == null || !form.getPublishAt().isAfter(LocalDateTime.now()))) {
            throw new BadRequestException("Hãy chọn thời gian tương lai để tự động đăng");
        }
        Announcement a = form.getId() == null ? new Announcement() : get(form.getId());
        if (a.getId() != null && a.getStatus() != AnnouncementStatus.DRAFT) {
            throw new InvalidTransitionException("Chỉ có thể sửa bản nháp");
        }
        String before = a.getId() == null ? null : a.getTitle();
        a.setTitle(form.getTitle().strip()); a.setSummary(form.getSummary().strip());
        a.setBodyHtml(body); a.setAudience(form.getAudience()); a.setPinned(form.isPinned());
        a.setPublishAt(form.getPublishAt()); a.setExpiresAt(form.getExpiresAt());
        a.setNotifyOnPublish(form.isNotifyOnPublish()); a.setAutoPublish(form.isAutoPublish());
        if (a.getId() == null) { a.setStatus(AnnouncementStatus.DRAFT); a.setCreatedBy(actorId); }
        a = announcements.saveAndFlush(a);
        if (form.getCoverAssetId() != null && !form.getCoverAssetId().equals(a.getCoverAssetId())) {
            media.attach(form.getCoverAssetId(), MediaUsageType.ANNOUNCEMENT_COVER, a.getId(), actorId);
        }
        a.setCoverAssetId(form.getCoverAssetId());
        audit.log(AuditActions.ANNOUNCEMENT_CHANGED, "announcement", a.getId(), before, a.getTitle());
        return a.getId();
    }

    @Transactional
    public void publish(Long id, Long actorId) {
        Announcement a = get(id);
        if (a.getStatus() != AnnouncementStatus.DRAFT) throw new InvalidTransitionException("Thông báo không còn là bản nháp");
        LocalDateTime now = LocalDateTime.now();
        if (a.getPublishAt() == null || a.getPublishAt().isBefore(now)) a.setPublishAt(now);
        a.setStatus(AnnouncementStatus.PUBLISHED); a.setAutoPublish(false); a.setNotified(false);
        audit.log(AuditActions.ANNOUNCEMENT_CHANGED, "announcement", id, "DRAFT", "PUBLISHED");
    }

    @Transactional
    public void unpublish(Long id, Long actorId) {
        Announcement a = get(id);
        if (a.getStatus() != AnnouncementStatus.PUBLISHED) throw new InvalidTransitionException("Thông báo chưa được xuất bản");
        a.setStatus(AnnouncementStatus.DRAFT); a.setAutoPublish(false);
        audit.log(AuditActions.ANNOUNCEMENT_CHANGED, "announcement", id, "PUBLISHED", "DRAFT");
    }

    @Transactional
    public void delete(Long id, Long actorId) {
        Announcement a = get(id);
        if (a.getStatus() != AnnouncementStatus.DRAFT) throw new InvalidTransitionException("Chỉ có thể xóa bản nháp");
        announcements.delete(a);
        audit.log(AuditActions.ANNOUNCEMENT_CHANGED, "announcement", id, a.getTitle(), "DELETED");
    }

    private Announcement get(Long id) {
        return announcements.findById(id).orElseThrow(NotFoundException::new);
    }
}
