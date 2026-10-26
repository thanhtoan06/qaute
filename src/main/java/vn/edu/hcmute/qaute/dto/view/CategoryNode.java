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

    // Jakarta EL getter methods
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getIcon() { return icon; }
    public int getDepth() { return depth; }
    public List<CategoryNode> getChildren() { return children; }
}
