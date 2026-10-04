package vn.edu.hcmute.qaute.entity.organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

/**
 * Chuyên mục / chủ đề tư vấn (bảng categories), cây tối đa 2 cấp.
 * depth = 1: chuyên mục (parentId = null); depth = 2: chủ đề (parentId = id chuyên mục cha).
 * Cùng miền tổ chức nhưng vẫn lưu khóa ngoại bằng Long id, không dùng @ManyToOne.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends AuditableEntity {

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "parent_id")
    private Long parentId;

    /** Cột TINYINT trong DB; columnDefinition giúp ddl-auto=validate không báo lệch kiểu với int. */
    @Column(name = "depth", nullable = false, columnDefinition = "TINYINT")
    private int depth;

    @Column(name = "code", nullable = false, length = 40)
    private String code;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "icon", length = 40)
    private String icon;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
}
