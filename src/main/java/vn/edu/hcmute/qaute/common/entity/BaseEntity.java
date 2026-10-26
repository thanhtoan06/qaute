package vn.edu.hcmute.qaute.common.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.Objects;

// Lớp cơ sở cho các thực thể dùng chung trong ứng dụng.
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lấy mã định danh của thực thể.
    public Long getId() {
        return id;
    }

    // Gán mã định danh cho thực thể.
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    // So sánh hai thực thể dựa trên mã định danh.
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        BaseEntity that = (BaseEntity) other;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    // Trả về mã băm dựa trên lớp của thực thể.
    public int hashCode() {
        return getClass().hashCode();
    }
}
