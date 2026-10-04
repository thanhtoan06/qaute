package vn.edu.hcmute.qaute.dto.view;

import java.util.List;

/**
 * Nút phòng ban trong cây "phòng ban → chuyên mục → chủ đề".
 * Chỉ chứa các chuyên mục cấp 1 đang hoạt động của phòng ban đó.
 */
public record DepartmentNode(
        Long id,
        String code,
        String name,
        String description,
        String email,
        String phone,
        String location,
        List<CategoryNode> categories) {
}
