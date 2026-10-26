package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

// Biểu thị không tìm thấy tài nguyên được yêu cầu.
public class NotFoundException extends AppException {

    // Khởi tạo ngoại lệ không tìm thấy với thông điệp mặc định.
    public NotFoundException() {
        super(ErrorCode.ERR_NOT_FOUND);
    }

    // Khởi tạo ngoại lệ không tìm thấy với thông điệp được cung cấp.
    public NotFoundException(String message) {
        super(ErrorCode.ERR_NOT_FOUND, message);
    }
}
