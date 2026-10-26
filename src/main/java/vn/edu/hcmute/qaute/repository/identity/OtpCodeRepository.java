package vn.edu.hcmute.qaute.repository.identity;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.common.constant.OtpPurpose;
import vn.edu.hcmute.qaute.entity.identity.OtpCode;

// Khai báo truy vấn lưu trữ dữ liệu OtpCode.
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    // Xóa mã OTP của người dùng.
    long deleteByUserId(Long userId);

    // Lấy mã OTP chưa dùng mới nhất theo người dùng và mục đích.
    Optional<OtpCode> findFirstByUserIdAndPurposeAndConsumedAtIsNullOrderByIdDesc(
            Long userId, OtpPurpose purpose);

    // Lấy mã OTP mới nhất theo email và mục đích.
    Optional<OtpCode> findFirstByEmailAndPurposeOrderByIdDesc(String email, OtpPurpose purpose);

    // Đếm mã OTP được tạo sau thời điểm chỉ định.
    long countByEmailAndPurposeAndCreatedAtAfter(String email, OtpPurpose purpose,
                                                  LocalDateTime createdAt);
}
