package vn.edu.hcmute.qaute.common.constant;

public enum ErrorCode {
    ERR_VALIDATION(400, "Dữ liệu không hợp lệ"),
    ERR_DUPLICATE(409, "Dữ liệu đã tồn tại"),
    ERR_UNAUTHORIZED(401, "Bạn cần đăng nhập"),
    ERR_FORBIDDEN(403, "Bạn không có quyền thực hiện"),
    ERR_NOT_FOUND(404, "Không tìm thấy dữ liệu"),
    ERR_INVALID_TRANSITION(409, "Thao tác không hợp lệ với trạng thái hiện tại"),
    ERR_CONFLICT(409, "Dữ liệu vừa được người khác thay đổi, vui lòng tải lại"),
    ERR_RATE_LIMIT(429, "Bạn thao tác quá nhanh, vui lòng thử lại sau"),
    ERR_OTP_INVALID(400, "Mã OTP không đúng"),
    ERR_OTP_EXPIRED(400, "Mã OTP đã hết hạn"),
    ERR_OTP_LOCKED(400, "Bạn đã nhập sai quá số lần cho phép"),
    ERR_ACCOUNT_LOCKED(423, "Tài khoản đang bị khóa tạm thời"),
    ERR_FILE_REJECTED(400, "Tệp không hợp lệ"),
    ERR_SLOT_TAKEN(409, "Khung giờ đã có người đặt"),
    ERR_LIMIT_EXCEEDED(400, "Vượt quá giới hạn cho phép");

    private final int httpStatus;
    private final String defaultMessage;

    ErrorCode(int httpStatus, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
