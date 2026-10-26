package vn.edu.hcmute.qaute.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác ResetOtp.
@Getter
@Setter
@NoArgsConstructor
public class ResetOtpForm {

    @NotBlank(message = "Thiếu thông tin định danh")
    private String identifier;

    @NotBlank(message = "Vui lòng nhập mã OTP")
    @Pattern(regexp = "\\d{6}", message = "Mã OTP phải là 6 chữ số")
    private String otp;
}
