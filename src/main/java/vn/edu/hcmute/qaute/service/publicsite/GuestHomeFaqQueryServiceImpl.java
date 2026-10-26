package vn.edu.hcmute.qaute.service.publicsite;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.dto.view.GuestHomeFaqView;
import vn.edu.hcmute.qaute.entity.publicsite.GuestHomeFaq;
import vn.edu.hcmute.qaute.repository.publicsite.GuestHomeFaqRepository;

// Xử lý nghiệp vụ truy vấn câu hỏi thường gặp trên trang chủ guest.
@Service
@Transactional(readOnly = true)
public class GuestHomeFaqQueryServiceImpl implements GuestHomeFaqQueryService {

    private final GuestHomeFaqRepository repository;

    // Khởi tạo dịch vụ truy vấn GuestHomeFaq.
    public GuestHomeFaqQueryServiceImpl(GuestHomeFaqRepository repository) {
        this.repository = repository;
    }

    @Override
    // Lấy tất cả FAQ đang hiển thị.
    public List<GuestHomeFaqView> getAllActive() {
        return repository.findActiveOrdered().stream()
                .map(this::toView)
                .toList();
    }

    @Override
    // Lấy FAQ theo danh mục.
    public List<GuestHomeFaqView> getByCategory(String categoryName) {
        return repository.findByCategoryName(categoryName).stream()
                .map(this::toView)
                .toList();
    }

    @Override
    // Lấy FAQ được nhóm theo danh mục.
    public Map<String, List<GuestHomeFaqView>> getGroupedByCategory() {
        List<GuestHomeFaqView> all = getAllActive();
        return all.stream()
                .collect(Collectors.groupingBy(
                        GuestHomeFaqView::getCategoryName,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    // Chuyển entity sang DTO.
    private GuestHomeFaqView toView(GuestHomeFaq entity) {
        return GuestHomeFaqView.builder()
                .id(entity.getId())
                .categoryName(entity.getCategoryName())
                .question(entity.getQuestion())
                .answer(entity.getAnswer())
                .displayOrder(entity.getDisplayOrder())
                .isActive(entity.isActive())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
