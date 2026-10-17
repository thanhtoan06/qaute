package vn.edu.hcmute.qaute.dto.view;

/**
 * Một dòng phòng ban trong danh sách quản trị, kèm số chuyên mục thuộc phòng ban.
 */
public record DepartmentListItem(
        Long id,
        String code,
        String name,
        String description,
        String email,
        String phone,
        String location,
        int sortOrder,
        boolean active,
        long categoryCount) {
}
