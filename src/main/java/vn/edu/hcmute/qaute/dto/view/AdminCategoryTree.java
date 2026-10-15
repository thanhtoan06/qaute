package vn.edu.hcmute.qaute.dto.view;

import java.util.List;

/** Một phòng ban cùng cây chuyên mục (kể cả đã tắt) cho trang quản trị. */
public record AdminCategoryTree(
        Long departmentId,
        String departmentCode,
        String departmentName,
        boolean departmentActive,
        List<Node> categories) {

    /** depth 1 = chuyên mục, depth 2 = chủ đề. Số đếm của chuyên mục cấp 1 đã gộp cả chủ đề con. */
    public record Node(
            Long id,
            Long parentId,
            String code,
            String name,
            String icon,
            int depth,
            int sortOrder,
            boolean active,
            long faqCount,
            long openTicketCount,
            List<Node> children) {
    }
}
