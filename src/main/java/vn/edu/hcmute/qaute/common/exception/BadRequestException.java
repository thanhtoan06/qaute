package vn.edu.hcmute.qaute.common.exception;

import java.util.Map;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;

// Biểu thị yêu cầu không hợp lệ gửi đến ứng dụng.
public class BadRequestException extends AppException {

    private final Map<String, String> fieldErrors;

    // Khởi tạo lỗi yêu cầu không hợp lệ với danh sách lỗi trường rỗng.
    public BadRequestException() {
        this(ErrorCode.ERR_VALIDATION.getDefaultMessage(), Map.of());
    }

    // Khởi tạo lỗi yêu cầu không hợp lệ với thông điệp được cung cấp.
    public BadRequestException(String message) {
        this(message, Map.of());
    }

    // Khởi tạo lỗi yêu cầu không hợp lệ với thông điệp và lỗi từng trường.
    public BadRequestException(String message, Map<String, String> fieldErrors) {
        super(ErrorCode.ERR_VALIDATION, message);
        this.fieldErrors = fieldErrors == null ? Map.of() : fieldErrors;
    }

    // Trả về các lỗi được gắn với từng trường.
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
