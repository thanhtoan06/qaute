package vn.edu.hcmute.qaute.repository.identity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.entity.identity.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByMssv(String mssv);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByMssv(String mssv);

    List<User> findByStatusAndCreatedAtBefore(UserStatus status, LocalDateTime before);
}
