package vn.edu.hcmute.qaute.entity.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

// Biểu diễn dữ liệu SearchSynonym được lưu trữ trong hệ thống.
@Entity
@Table(name = "search_synonyms", uniqueConstraints = {
        @UniqueConstraint(name = "uk_search_synonyms_term_synonym", columnNames = {"term", "synonym"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchSynonym extends AuditableEntity {

    @Column(name = "term", nullable = false, length = 100)
    private String term;

    @Column(name = "synonym", nullable = false, length = 100)
    private String synonym;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
}

