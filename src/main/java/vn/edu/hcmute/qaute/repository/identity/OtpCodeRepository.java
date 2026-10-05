package vn.edu.hcmute.qaute.repository.identity;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.common.constant.OtpPurpose;
import vn.edu.hcmute.qaute.entity.identity.OtpCode;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    long deleteByUserId(Long userId);

    Optional<OtpCode> findFirstByUserIdAndPurposeAndConsumedAtIsNullOrderByIdDesc(
            Long userId, OtpPurpose purpose);

    Optional<OtpCode> findFirstByEmailAndPurposeOrderByIdDesc(String email, OtpPurpose purpose);

    long countByEmailAndPurposeAndCreatedAtAfter(String email, OtpPurpose purpose,
                                                  LocalDateTime createdAt);
}
