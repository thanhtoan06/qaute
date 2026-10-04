package vn.edu.hcmute.qaute.dto.view;

/**
 * Thông tin phẳng của một chuyên mục (kèm tên phòng ban) để các miền khác dùng
 * mà không phải đụng tới entity.
 *
 * @param active true khi chuyên mục đang bật VÀ phòng ban chứa nó cũng đang bật
 */
public record CategoryView(
        Long id,
        Long departmentId,
        String departmentName,
        Long parentId,
        String code,
        String name,
        String icon,
        int depth,
        boolean active) {
}
