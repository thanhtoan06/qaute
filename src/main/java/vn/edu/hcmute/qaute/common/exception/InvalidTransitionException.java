package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

// Biểu thị thao tác chuyển trạng thái không được phép trong quy trình hiện tại.
public class InvalidTransitionException extends AppException {

    // Khởi tạo ngoại lệ chuyển trạng thái không hợp lệ với thông điệp mặc định.
    public InvalidTransitionException() {
        super(ErrorCode.ERR_INVALID_TRANSITION);
    }

    // Khởi tạo ngoại lệ chuyển trạng thái không hợp lệ với thông điệp được cung cấp.
    public InvalidTransitionException(String message) {
        super(ErrorCode.ERR_INVALID_TRANSITION, message);
    }
}
