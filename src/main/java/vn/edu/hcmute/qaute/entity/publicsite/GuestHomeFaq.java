package vn.edu.hcmute.qaute.entity.publicsite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Biểu diễn câu hỏi thường gặp hiển thị trên trang chủ dành cho khách.
@Entity
@Table(name = "guest_home_faqs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestHomeFaq extends AuditableEntity {

    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    @Column(name = "question", nullable = false, length = 300)
    private String question;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public boolean isActive() {
        return this.isActive;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
