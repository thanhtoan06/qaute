package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

// Biểu thị xung đột với trạng thái dữ liệu hiện tại.
public class ConflictException extends AppException {

    // Khởi tạo ngoại lệ xung đột với thông điệp mặc định.
    public ConflictException() {
        super(ErrorCode.ERR_CONFLICT);
    }

    // Khởi tạo ngoại lệ xung đột với thông điệp được cung cấp.
    public ConflictException(String message) {
        super(ErrorCode.ERR_CONFLICT, message);
    }
}
