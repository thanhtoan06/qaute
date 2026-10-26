package vn.edu.hcmute.qaute.entity.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

// Biểu diễn dữ liệu Faq được lưu trữ trong hệ thống.
@Entity
@Table(name = "faqs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Faq extends AuditableEntity {

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "reviewer_id")
    private Long reviewerId;

    @Column(name = "source_ticket_id")
    private Long sourceTicketId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "slug", nullable = false, length = 220, unique = true)
    private String slug;

    @Column(name = "summary", length = 500)
    private String summary;

    @Column(name = "body_html", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String bodyHtml;

    @Column(name = "body_text", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String bodyText;

    @Column(name = "title_norm", nullable = false, length = 255)
    private String titleNorm;

    @Column(name = "body_norm", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String bodyNorm;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private FaqStatus status;

    @Column(name = "featured", nullable = false)
    private boolean featured;

    @Column(name = "publish_at")
    private LocalDateTime publishAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "view_count", nullable = false)
    private long viewCount;

    @Column(name = "helpful_count", nullable = false)
    private int helpfulCount;

    @Column(name = "not_helpful_count", nullable = false)
    private int notHelpfulCount;

    @Builder.Default
    @Column(name = "version_no", nullable = false)
    private int versionNo = 1;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "faq_tag_map",
            joinColumns = @JoinColumn(name = "faq_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<FaqTag> tags = new HashSet<>();
}

