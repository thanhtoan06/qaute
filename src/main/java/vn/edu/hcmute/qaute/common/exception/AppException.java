package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

// Lớp cơ sở cho các ngoại lệ nghiệp vụ của ứng dụng.
public class AppException extends RuntimeException {

    private final ErrorCode code;

    // Khởi tạo ngoại lệ với thông điệp mặc định của mã lỗi.
    public AppException(ErrorCode code) {
        this(code, code.getDefaultMessage());
    }

    // Khởi tạo ngoại lệ với mã lỗi và thông điệp được cung cấp.
    public AppException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    // Trả về mã lỗi của ngoại lệ.
    public ErrorCode getCode() {
        return code;
    }
}
