package vn.edu.hcmute.qaute.repository.publicsite;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmute.qaute.entity.publicsite.GuestHomeFaq;

// Khai báo truy vấn lưu trữ dữ liệu GuestHomeFaq.
public interface GuestHomeFaqRepository extends JpaRepository<GuestHomeFaq, Long> {

    // Lấy tất cả FAQ, sắp xếp theo thứ tự.
    List<GuestHomeFaq> findAllByOrderByDisplayOrderAsc();

    // Lấy danh sách FAQ đang hiển thị, sắp xếp theo thứ tự.
    @Query("SELECT g FROM GuestHomeFaq g WHERE g.isActive = true ORDER BY g.displayOrder ASC")
    List<GuestHomeFaq> findActiveOrdered();

    // Lấy danh sách FAQ theo danh mục.
    @Query("SELECT g FROM GuestHomeFaq g WHERE g.isActive = true AND g.categoryName = :categoryName ORDER BY g.displayOrder ASC")
    List<GuestHomeFaq> findByCategoryName(String categoryName);
}
