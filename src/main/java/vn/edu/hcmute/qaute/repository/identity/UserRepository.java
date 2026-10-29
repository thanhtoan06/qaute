package vn.edu.hcmute.qaute.repository.identity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.entity.identity.User;

// Khai báo truy vấn lưu trữ dữ liệu User.
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // Tìm người dùng theo email không phân biệt hoa thường.
    Optional<User> findByEmailIgnoreCase(String email);

    // Tìm người dùng theo mã số sinh viên.
    Optional<User> findByMssv(String mssv);

    // Tìm người dùng theo họ tên không phân biệt hoa thường.
    List<User> findAllByFullNameIgnoreCase(String fullName);

    // Kiểm tra email đã được sử dụng chưa.
    boolean existsByEmailIgnoreCase(String email);

    // Kiểm tra mã số sinh viên đã được sử dụng chưa.
    boolean existsByMssv(String mssv);

    // Lấy người dùng theo trạng thái và thời điểm tạo.
    List<User> findByStatusAndCreatedAtBefore(UserStatus status, LocalDateTime before);

    @Query("select u.id from User u where u.role.code = :roleCode and u.status = :status")
    // Lấy mã định danh theo vai trò và trạng thái.
    List<Long> findIdsByRoleCodeAndStatus(@Param("roleCode") RoleCode roleCode,
                                          @Param("status") UserStatus status);

    /** Danh sách Manager theo tên hoặc email; keyword null/rỗng nghĩa là lấy toàn bộ. */
    @Query("""
            select u from User u
            where u.role.code = :roleCode
              and (:keyword is null
                   or lower(u.fullName) like lower(concat('%', :keyword, '%'))
                   or lower(u.email) like lower(concat('%', :keyword, '%')))
            order by lower(u.fullName), lower(u.email)
            """)
    // Tìm người dùng theo vai trò và từ khóa.
    List<User> searchByRoleAndKeyword(@Param("roleCode") RoleCode roleCode,
                                      @Param("keyword") String keyword);
}
