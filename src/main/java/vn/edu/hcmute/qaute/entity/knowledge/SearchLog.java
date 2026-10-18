package vn.edu.hcmute.qaute.entity.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

@Entity
@Table(name = "search_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchLog extends AuditableEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "query_norm", nullable = false, length = 200)
    private String queryNorm;

    @Column(name = "result_count")
    private Integer resultCount;

    @Column(name = "clicked_faq_id")
    private Long clickedFaqId;

    @Column(name = "source", nullable = false, length = 20)
    private String source;

    @Column(name = "ip_hash", length = 64)
    private String ipHash;
}

