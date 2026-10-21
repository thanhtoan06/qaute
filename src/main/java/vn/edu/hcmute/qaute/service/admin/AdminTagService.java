package vn.edu.hcmute.qaute.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import vn.edu.hcmute.qaute.common.util.BoundedPages;

import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.SlugUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.view.AdminTagView;
import vn.edu.hcmute.qaute.entity.knowledge.FaqTag;
import vn.edu.hcmute.qaute.repository.knowledge.FaqTagRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;

@Service
@Transactional(readOnly = true)
public class AdminTagService {
    private final FaqTagRepository tags;
    private final EntityManager entityManager;
    private final AuditService audit;

    public AdminTagService(FaqTagRepository tags, EntityManager entityManager, AuditService audit) {
        this.tags = tags;
        this.entityManager = entityManager;
        this.audit = audit;
    }

    public List<AdminTagView> list() {
        Map<Long, Long> counts = new HashMap<>();
        for (Object[] row : tags.countAllUsages()) {
            counts.put(((Number) row[0]).longValue(), ((Number) row[1]).longValue());
        }
        return tags.findAllByOrderByNameAsc().stream()
                .map(tag -> new AdminTagView(tag.getId(), tag.getName(), tag.getSlug(),
                        counts.getOrDefault(tag.getId(), 0L))).toList();
    }

    public Page<AdminTagView> page(Pageable requested) {
        Page<FaqTag> page = tags.findAll(PageRequest.of(BoundedPages.of(requested).getPageNumber(),
                BoundedPages.of(requested).getPageSize(), Sort.by("name", "id")));
        return page.map(tag -> {
            Number count = (Number) entityManager.createNativeQuery(
                    "SELECT COUNT(*) FROM faq_tag_map WHERE tag_id = :id")
                    .setParameter("id", tag.getId()).getSingleResult();
            return new AdminTagView(tag.getId(), tag.getName(), tag.getSlug(), count.longValue());
        });
    }

    @Transactional
    public void create(String name) {
        String cleaned = cleanName(name);
        String norm = TextUtil.normalizeVi(cleaned);
        String slug = SlugUtil.slugify(cleaned);
        ensureUnique(norm, slug, null);
        try {
            FaqTag tag = tags.saveAndFlush(FaqTag.builder()
                    .name(cleaned).nameNorm(norm).slug(slug).build());
            audit.log(AuditActions.TAG_CHANGED, "FaqTag", tag.getId(), null, cleaned);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateException("name", "Thẻ này đã tồn tại");
        }
    }

    @Transactional
    public void rename(Long id, String name) {
        FaqTag tag = require(id);
        String cleaned = cleanName(name);
        String norm = TextUtil.normalizeVi(cleaned);
        String slug = SlugUtil.slugify(cleaned);
        if (tag.getName().equals(cleaned)) return;
        ensureUnique(norm, slug, id);
        String before = tag.getName();
        tag.setName(cleaned);
        tag.setNameNorm(norm);
        tag.setSlug(slug);
        try {
            tags.saveAndFlush(tag);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateException("name", "Thẻ này đã tồn tại");
        }
        audit.log(AuditActions.TAG_CHANGED, "FaqTag", id, before, cleaned);
    }

    @Transactional
    public void merge(Long sourceId, Long targetId) {
        if (sourceId == null || targetId == null) throw new BadRequestException("Chọn thẻ nguồn và thẻ đích");
        if (sourceId.equals(targetId)) throw new BadRequestException("Chọn thẻ đích khác thẻ nguồn");
        FaqTag source = require(sourceId);
        FaqTag target = require(targetId);
        entityManager.createNativeQuery("INSERT IGNORE INTO faq_tag_map (faq_id, tag_id) "
                + "SELECT faq_id, :target FROM faq_tag_map WHERE tag_id = :source")
                .setParameter("target", targetId).setParameter("source", sourceId).executeUpdate();
        entityManager.createNativeQuery("DELETE FROM faq_tag_map WHERE tag_id = :source")
                .setParameter("source", sourceId).executeUpdate();
        tags.delete(source);
        tags.flush();
        audit.log(AuditActions.TAG_CHANGED, "FaqTag", sourceId,
                source.getName(), "Gộp vào " + target.getName() + " (#" + targetId + ")");
    }

    @Transactional
    public void delete(Long id) {
        FaqTag tag = require(id);
        Number count = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM faq_tag_map WHERE tag_id = :id")
                .setParameter("id", id).getSingleResult();
        if (count.longValue() > 0) throw new BadRequestException("Thẻ đang được FAQ sử dụng");
        tags.delete(tag);
        tags.flush();
        audit.log(AuditActions.TAG_CHANGED, "FaqTag", id, tag.getName(), null);
    }

    private FaqTag require(Long id) {
        return tags.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy thẻ"));
    }

    private void ensureUnique(String norm, String slug, Long currentId) {
        boolean duplicated = currentId == null ? tags.existsByNameNorm(norm)
                : tags.existsByNameNormAndIdNot(norm, currentId);
        if (duplicated || tags.findBySlug(slug)
                .filter(tag -> currentId == null || !currentId.equals(tag.getId())).isPresent()) {
            throw new DuplicateException("name", "Thẻ này đã tồn tại");
        }
    }

    private static String cleanName(String name) {
        String cleaned = name == null ? "" : name.trim().replaceAll("\\s+", " ");
        if (cleaned.length() < 2 || cleaned.length() > 40) {
            throw new BadRequestException("Tên thẻ phải từ 2 đến 40 ký tự");
        }
        return cleaned;
    }
}
