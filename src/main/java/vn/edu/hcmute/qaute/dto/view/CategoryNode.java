package vn.edu.hcmute.qaute.dto.view;

import java.util.List;

/**
 * Nút chuyên mục trong cây. depth = 1 là chuyên mục, depth = 2 là chủ đề.
 * Chuyên mục cấp 2 luôn có danh sách children rỗng.
 */
public record CategoryNode(
        Long id,
        String code,
        String name,
        String icon,
        int depth,
        List<CategoryNode> children) {
}
