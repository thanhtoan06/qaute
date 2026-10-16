package vn.edu.hcmute.qaute.repository.identity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.entity.identity.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByMssv(String mssv);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByMssv(String mssv);

    List<User> findByStatusAndCreatedAtBefore(UserStatus status, LocalDateTime before);

    /** Danh sách Manager theo tên hoặc email; keyword null/rỗng nghĩa là lấy toàn bộ. */
    @Query("""
            select u from User u
            where u.role.code = :roleCode
              and (:keyword is null
                   or lower(u.fullName) like lower(concat('%', :keyword, '%'))
                   or lower(u.email) like lower(concat('%', :keyword, '%')))
            order by lower(u.fullName), lower(u.email)
            """)
    List<User> searchByRoleAndKeyword(@Param("roleCode") RoleCode roleCode,
                                      @Param("keyword") String keyword);
}
