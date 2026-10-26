package vn.edu.hcmute.qaute.entity.governance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

// Biểu diễn dữ liệu FaqRevision được lưu trữ trong hệ thống.
@Entity
@Table(name = "faq_revisions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqRevision extends AuditableEntity {
    @Column(name = "faq_id", nullable = false)
    private Long faqId;

    @Column(name = "version_no", nullable = false)
    private int versionNo;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "summary", length = 500)
    private String summary;

    @Column(name = "body_html", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String bodyHtml;

    @Column(name = "changed_by")
    private Long changedBy;

    @Column(name = "change_note", length = 500)
    private String changeNote;
}
