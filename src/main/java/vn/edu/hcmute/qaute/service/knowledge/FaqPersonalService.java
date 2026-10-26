package vn.edu.hcmute.qaute.service.knowledge;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.dto.view.FaqListItem;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;
import vn.edu.hcmute.qaute.entity.knowledge.FaqBookmark;
import vn.edu.hcmute.qaute.entity.knowledge.FaqTag;
import vn.edu.hcmute.qaute.entity.knowledge.FaqView;
import vn.edu.hcmute.qaute.repository.knowledge.FaqBookmarkRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqViewRepository;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

// Xử lý nghiệp vụ FaqPersonal trong hệ thống.
@Service
public class FaqPersonalService {
    private final FaqRepository faqs;
    private final FaqBookmarkRepository bookmarks;
    private final FaqViewRepository views;
    private final CategoryQueryService categories;

    // Khởi tạo dịch vụ quản lý FAQ cá nhân.
    public FaqPersonalService(FaqRepository faqs, FaqBookmarkRepository bookmarks,
                              FaqViewRepository views, CategoryQueryService categories) {
        this.faqs = faqs;
        this.bookmarks = bookmarks;
        this.views = views;
        this.categories = categories;
    }

    @Transactional
    // Bật hoặc tắt đánh dấu FAQ của người dùng.
    public boolean toggleBookmark(Long userId, Long faqId) {
        requirePublished(faqId);
        return bookmarks.findByUserIdAndFaqId(userId, faqId).map(bookmark -> {
            bookmarks.delete(bookmark);
            return false;
        }).orElseGet(() -> {
            bookmarks.save(FaqBookmark.builder().userId(userId).faqId(faqId).build());
            return true;
        });
    }

    @Transactional
    // Ghi nhận lượt xem FAQ của người dùng.
    public void recordView(Long userId, Long faqId) {
        requirePublished(faqId);
        LocalDateTime now = LocalDateTime.now();
        FaqView view = views.findByUserIdAndFaqId(userId, faqId).orElse(null);
        if (view == null) {
            views.save(FaqView.builder().userId(userId).faqId(faqId).lastViewedAt(now).viewCount(1).build());
            return;
        }
        view.setLastViewedAt(now);
        view.setViewCount(view.getViewCount() + 1);
        views.save(view);
    }

    @Transactional(readOnly = true)
    // Trả về các FAQ người dùng xem gần đây.
    public Page<FaqListItem> recent(Long userId, Pageable pageable) {
        Pageable effectivePageable = pageable == null ? PageRequest.of(0, 20) : pageable;
        Page<FaqView> viewPage = views.findByUserIdOrderByLastViewedAtDesc(userId, effectivePageable);
        if (viewPage.isEmpty()) return new PageImpl<>(List.of(), effectivePageable, viewPage.getTotalElements());

        List<Long> faqIds = viewPage.getContent().stream().map(FaqView::getFaqId).toList();
        Map<Long, Faq> byId = faqs.findAllById(faqIds).stream().filter(this::isPublishedNow)
                .collect(Collectors.toMap(Faq::getId, faq -> faq));
        List<FaqListItem> items = faqIds.stream().map(byId::get).filter(java.util.Objects::nonNull)
                .map(this::toListItem).toList();
        return new PageImpl<>(items, effectivePageable, viewPage.getTotalElements());
    }

    // Tìm FAQ hiện đang được xuất bản.
    private Faq requirePublished(Long faqId) {
        return faqs.findById(faqId).filter(this::isPublishedNow)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));
    }

    // Kiểm tra FAQ đang được xuất bản và đã đến thời điểm hiển thị.
    private boolean isPublishedNow(Faq faq) {
        return faq.getStatus() == FaqStatus.PUBLISHED
                && (faq.getPublishAt() == null || !faq.getPublishAt().isAfter(LocalDateTime.now()));
    }

    // Chuyển FAQ thành dữ liệu hiển thị trong danh sách.
    private FaqListItem toListItem(Faq faq) {
        String categoryName = categories.findCategory(faq.getCategoryId()).map(CategoryView::name).orElse("");
        List<String> tags = faq.getTags().stream().map(FaqTag::getName).sorted().toList();
        return new FaqListItem(faq.getId(), faq.getTitle(), faq.getSlug(), faq.getSummary(), faq.getCategoryId(),
                categoryName, faq.getViewCount(), faq.getHelpfulCount(), faq.getNotHelpfulCount(), faq.getUpdatedAt(), tags);
    }
}
