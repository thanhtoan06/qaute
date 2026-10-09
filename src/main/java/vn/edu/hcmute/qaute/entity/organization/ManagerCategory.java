package vn.edu.hcmute.qaute.entity.organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.ManagerCategoryLevel;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

/**
 * Một dòng gán chuyên mục phụ trách cho Manager (bảng manager_categories).
 * Gán ở chuyên mục cha thì ngầm bao gồm cả các chuyên mục con (xem ManagerScopeService).
 */
@Entity
@Table(name = "manager_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerCategory extends AuditableEntity {

    @Column(name = "manager_id", nullable = false)
    private Long managerId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false, length = 10)
    private ManagerCategoryLevel level;
}
