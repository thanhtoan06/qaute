package vn.edu.hcmute.qaute.entity.manager;

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
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;
import vn.edu.hcmute.qaute.common.constant.CannedResponseScope;

@Entity @Table(name = "canned_responses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CannedResponse extends AuditableEntity {
    @Column(name = "owner_id", nullable = false) private Long ownerId;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10) private CannedResponseScope scope;
    @Column(name = "category_id") private Long categoryId;
    @Column(nullable = false, length = 120) private String title;
    @Column(name = "body_html", nullable = false, columnDefinition = "MEDIUMTEXT") private String bodyHtml;
    @Column(name = "usage_count", nullable = false) private int usageCount;
    @Builder.Default @Column(nullable = false) private boolean active = true;
}
