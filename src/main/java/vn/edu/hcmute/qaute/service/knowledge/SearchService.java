package vn.edu.hcmute.qaute.service.knowledge;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.common.constant.FaqSort;
import vn.edu.hcmute.qaute.dto.view.SearchResultItem;

// Xử lý nghiệp vụ Search trong hệ thống.
public interface SearchService {

    // Tìm kiếm FAQ theo nội dung, danh mục, thẻ và thứ tự sắp xếp.
    Page<SearchResultItem> search(String rawQuery,
                                  List<Long> categoryIds,
                                  String tagSlug,
                                  FaqSort sort,
                                  Pageable p,
                                  Long userId,
                                  String ipHash,
                                  String source);
}
