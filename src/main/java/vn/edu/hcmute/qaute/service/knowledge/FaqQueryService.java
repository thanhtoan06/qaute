package vn.edu.hcmute.qaute.service.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.dto.request.FaqFilter;
import vn.edu.hcmute.qaute.dto.view.FaqDetailView;
import vn.edu.hcmute.qaute.dto.view.FaqListItem;
import vn.edu.hcmute.qaute.dto.view.FaqSuggestion;
import vn.edu.hcmute.qaute.dto.view.TagView;

// Xử lý nghiệp vụ FaqQuery trong hệ thống.
public interface FaqQueryService {

    // Lấy danh sách FAQ theo bộ lọc và phân trang.
    Page<FaqListItem> list(FaqFilter filter, Pageable pageable);

    // Lấy danh sách FAQ đã được người dùng đánh dấu.
    Page<FaqListItem> listBookmarked(Long userId, Pageable pageable);

    // Lấy các mã FAQ được người dùng đánh dấu trong danh sách đã cho.
    Set<Long> bookmarkedIds(Long userId, Collection<Long> faqIds);

    // Lấy trạng thái bình chọn của người dùng cho các FAQ đã cho.
    Map<Long, Boolean> myVotes(Long userId, Collection<Long> faqIds);

    // Lấy chi tiết FAQ đang xuất bản theo slug.
    FaqDetailView getPublishedBySlug(String slug);

    // Lấy các FAQ được xem nhiều hơn ngưỡng chỉ định.
    List<FaqListItem> topViewed(long minViews, int limit);

    // Lấy các FAQ được đánh giá hữu ích nhiều nhất.
    List<FaqListItem> topHelpful(int limit);

    // Lấy các FAQ được chọn làm nổi bật.
    List<FaqListItem> featured(int limit);

    // Lấy các FAQ mới cập nhật.
    List<FaqListItem> latest(int limit);

    // Lấy các FAQ liên quan đến FAQ đã cho.
    List<FaqListItem> related(Long faqId, int limit);

    // Lấy các thẻ phổ biến.
    List<TagView> popularTags(int limit);

    // Đếm số FAQ đang được xuất bản.
    long countPublished();

    // Đếm số FAQ đang xuất bản theo danh mục.
    Map<Long, Long> countPublishedByCategory();

    // Gợi ý FAQ phù hợp với nội dung tìm kiếm.
    List<FaqSuggestion> suggest(String query, int limit);
}
