package vn.edu.hcmute.qaute.service.organization;

import java.util.List;
import java.util.Optional;

import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.dto.view.DepartmentNode;

/**
 * Dịch vụ ĐỌC cây phòng ban – chuyên mục, dùng chung cho cả hai thành viên.
 * Chỉ đọc, không ghi dữ liệu và không kiểm tra quyền.
 */
public interface CategoryQueryService {

    /**
     * Cây đang hoạt động: chỉ phòng ban active; mỗi phòng ban gồm các chuyên mục cấp 1
     * active, mỗi chuyên mục kèm các chủ đề cấp 2 active. Sắp theo sortOrder rồi theo tên.
     * Phòng ban chưa có chuyên mục nào vẫn xuất hiện (categories rỗng).
     */
    // Lấy cây phòng ban và chuyên mục đang hoạt động.
    List<DepartmentNode> getActiveTree();

    /**
     * Tìm một chuyên mục theo id, KỂ CẢ chuyên mục đã tắt (xem CategoryView.active).
     * id null hoặc không tồn tại thì trả Optional.empty().
     */
    // Tìm chuyên mục theo mã định danh.
    Optional<CategoryView> findCategory(Long categoryId);

    /**
     * Id của chính chuyên mục và các chuyên mục con (tối đa 2 cấp), KỂ CẢ con đã tắt
     * (để phạm vi quyền và bộ lọc vẫn thấy dữ liệu cũ). Chính nó đứng đầu danh sách.
     * Không tồn tại thì trả danh sách rỗng.
     */
    // Lấy mã định danh của chuyên mục và các chuyên mục con.
    List<Long> getDescendantIds(Long categoryId);

    /**
     * Đường dẫn hiển thị, ví dụ
     * "Phòng Đào tạo › Đăng ký học phần › Đăng ký sai / rút học phần".
     * Không tồn tại thì trả chuỗi rỗng "".
     */
    // Tạo đường dẫn hiển thị của chuyên mục.
    String getDisplayPath(Long categoryId);

    /**
     * Các chuyên mục "lá" đang hoạt động (active, không có con active, thuộc phòng ban
     * active) để chọn khi tạo hoặc chuyển ticket. Sắp theo phòng ban, rồi chuyên mục, rồi chủ đề.
     */
    // Tìm các chuyên mục lá đang hoạt động.
    List<CategoryView> findAllActiveLeaf();

    /**
     * Id của tổ tiên và của chính chuyên mục, thứ tự từ gốc xuống chính nó,
     * ví dụ [id chuyên mục cấp 1, id chủ đề cấp 2]. Dùng cho ManagerScopeService.
     * Không tồn tại thì trả danh sách rỗng.
     */
    // Lấy mã định danh của tổ tiên và chính chuyên mục.
    List<Long> getAncestorIdsIncludingSelf(Long categoryId);
}
