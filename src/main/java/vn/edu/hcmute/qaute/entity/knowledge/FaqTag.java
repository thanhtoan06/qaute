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
@Table(name = "faq_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqTag extends AuditableEntity {

    @Column(name = "name", nullable = false, length = 60, unique = true)
    private String name;

    @Column(name = "name_norm", nullable = false, length = 60)
    private String nameNorm;

    @Column(name = "slug", nullable = false, length = 80, unique = true)
    private String slug;
}

