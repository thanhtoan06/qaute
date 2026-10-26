package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

// Biểu thị yêu cầu không được phép thực hiện.
public class ForbiddenException extends AppException {

    // Khởi tạo ngoại lệ không đủ quyền với thông điệp mặc định.
    public ForbiddenException() {
        super(ErrorCode.ERR_FORBIDDEN);
    }

    // Khởi tạo ngoại lệ không đủ quyền với thông điệp được cung cấp.
    public ForbiddenException(String message) {
        super(ErrorCode.ERR_FORBIDDEN, message);
    }
}
