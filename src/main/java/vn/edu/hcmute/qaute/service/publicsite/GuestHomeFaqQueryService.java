package vn.edu.hcmute.qaute.service.publicsite;

import java.util.List;
import java.util.Map;
import vn.edu.hcmute.qaute.dto.view.GuestHomeFaqView;

// Dịch vụ truy vấn câu hỏi thường gặp trên trang chủ guest.
public interface GuestHomeFaqQueryService {

    // Lấy tất cả FAQ đang hiển thị.
    List<GuestHomeFaqView> getAllActive();

    // Lấy FAQ theo danh mục.
    List<GuestHomeFaqView> getByCategory(String categoryName);

    // Lấy FAQ được nhóm theo danh mục.
    Map<String, List<GuestHomeFaqView>> getGroupedByCategory();
}
