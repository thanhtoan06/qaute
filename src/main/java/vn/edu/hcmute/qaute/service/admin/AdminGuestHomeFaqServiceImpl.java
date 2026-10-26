package vn.edu.hcmute.qaute.service.admin;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.GuestHomeFaqView;
import vn.edu.hcmute.qaute.entity.publicsite.GuestHomeFaq;
import vn.edu.hcmute.qaute.repository.publicsite.GuestHomeFaqRepository;

// Xử lý nghiệp vụ quản lý câu hỏi thường gặp trên trang chủ guest cho admin.
@Service
@Transactional
public class AdminGuestHomeFaqServiceImpl implements AdminGuestHomeFaqService {

    private final GuestHomeFaqRepository repository;

    // Khởi tạo dịch vụ quản lý GuestHomeFaq.
    public AdminGuestHomeFaqServiceImpl(GuestHomeFaqRepository repository) {
        this.repository = repository;
    }

    @Override
    // Lấy danh sách tất cả FAQ.
    public List<GuestHomeFaqView> listAll() {
        return repository.findAllByOrderByDisplayOrderAsc().stream()
                .map(this::toView)
                .toList();
    }

    @Override
    // Lấy FAQ theo ID.
    public GuestHomeFaqView getById(Long id) {
        GuestHomeFaq entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));
        return toView(entity);
    }

    @Override
    // Tạo FAQ mới.
    public Long create(String categoryName, String question, String answer, int displayOrder) {
        GuestHomeFaq entity = GuestHomeFaq.builder()
                .categoryName(categoryName)
                .question(question)
                .answer(answer)
                .displayOrder(displayOrder)
                .isActive(true)
                .build();
        return repository.save(entity).getId();
    }

    @Override
    // Cập nhật FAQ.
    public void update(Long id, String categoryName, String question, String answer, int displayOrder, boolean isActive) {
        GuestHomeFaq entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));
        entity.setCategoryName(categoryName);
        entity.setQuestion(question);
        entity.setAnswer(answer);
        entity.setDisplayOrder(displayOrder);
        entity.setActive(isActive);
        repository.save(entity);
    }

    @Override
    // Xóa FAQ.
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy FAQ");
        }
        repository.deleteById(id);
    }

    @Override
    // Tăng thứ tự hiển thị (đưa lên trên).
    public void moveUp(Long id) {
        GuestHomeFaq current = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));

        List<GuestHomeFaq> all = repository.findAllByOrderByDisplayOrderAsc();
        int currentIndex = -1;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(id)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex > 0) {
            GuestHomeFaq above = all.get(currentIndex - 1);
            int tempOrder = current.getDisplayOrder();
            current.setDisplayOrder(above.getDisplayOrder());
            above.setDisplayOrder(tempOrder);
            repository.save(current);
            repository.save(above);
        }
    }

    @Override
    // Giảm thứ tự hiển thị (đưa xuống dưới).
    public void moveDown(Long id) {
        GuestHomeFaq current = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));

        List<GuestHomeFaq> all = repository.findAllByOrderByDisplayOrderAsc();
        int currentIndex = -1;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(id)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex >= 0 && currentIndex < all.size() - 1) {
            GuestHomeFaq below = all.get(currentIndex + 1);
            int tempOrder = current.getDisplayOrder();
            current.setDisplayOrder(below.getDisplayOrder());
            below.setDisplayOrder(tempOrder);
            repository.save(current);
            repository.save(below);
        }
    }

    @Override
    // Bật/tắt trạng thái hiển thị.
    public void toggleActive(Long id) {
        GuestHomeFaq entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));
        entity.setActive(!entity.isActive());
        repository.save(entity);
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
