package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

// Biểu thị lỗi khi giá trị hoặc tài nguyên cần tạo đã tồn tại.
public class DuplicateException extends AppException {

    private final String field;

    // Khởi tạo ngoại lệ trùng lặp với thông điệp mặc định.
    public DuplicateException() {
        this(null, ErrorCode.ERR_DUPLICATE.getDefaultMessage());
    }

    // Khởi tạo ngoại lệ trùng lặp với thông điệp được cung cấp.
    public DuplicateException(String message) {
        this(null, message);
    }

    // Khởi tạo ngoại lệ trùng lặp với trường và thông điệp tương ứng.
    public DuplicateException(String field, String message) {
        super(ErrorCode.ERR_DUPLICATE, message);
        this.field = field;
    }

    // Trả về tên trường có giá trị bị trùng lặp.
    public String getField() {
        return field;
    }
}
