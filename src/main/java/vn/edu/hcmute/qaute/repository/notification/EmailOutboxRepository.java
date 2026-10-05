package vn.edu.hcmute.qaute.repository.notification;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.common.constant.EmailStatus;
import vn.edu.hcmute.qaute.entity.notification.EmailOutbox;

public interface EmailOutboxRepository extends JpaRepository<EmailOutbox, Long> {

    List<EmailOutbox> findTop20ByStatusAndNextAttemptAtLessThanEqualOrderByIdAsc(
            EmailStatus status, LocalDateTime now);
}
