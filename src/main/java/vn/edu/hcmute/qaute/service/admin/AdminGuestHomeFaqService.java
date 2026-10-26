package vn.edu.hcmute.qaute.service.admin;

import java.util.List;
import vn.edu.hcmute.qaute.dto.view.GuestHomeFaqView;

// Dịch vụ quản lý câu hỏi thường gặp trên trang chủ guest cho admin.
public interface AdminGuestHomeFaqService {

    // Lấy danh sách tất cả FAQ.
    List<GuestHomeFaqView> listAll();

    // Lấy FAQ theo ID.
    GuestHomeFaqView getById(Long id);

    // Tạo FAQ mới.
    Long create(String categoryName, String question, String answer, int displayOrder);

    // Cập nhật FAQ.
    void update(Long id, String categoryName, String question, String answer, int displayOrder, boolean isActive);

    // Xóa FAQ.
    void delete(Long id);

    // Tăng thứ tự hiển thị (đưa lên trên).
    void moveUp(Long id);

    // Giảm thứ tự hiển thị (đưa xuống dưới).
    void moveDown(Long id);

    // Bật/tắt trạng thái hiển thị.
    void toggleActive(Long id);
}
