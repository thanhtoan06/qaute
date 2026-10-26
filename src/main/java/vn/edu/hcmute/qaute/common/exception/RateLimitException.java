package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

// Biểu thị yêu cầu vượt giới hạn tần suất và thời gian cần chờ trước khi thử lại.
public class RateLimitException extends AppException {

    private final int retryAfterSeconds;

    // Khởi tạo ngoại lệ giới hạn tần suất với thời gian chờ bằng không.
    public RateLimitException() {
        this(0, ErrorCode.ERR_RATE_LIMIT.getDefaultMessage());
    }

    // Khởi tạo ngoại lệ giới hạn tần suất với thông điệp được cung cấp.
    public RateLimitException(String message) {
        this(0, message);
    }

    // Khởi tạo ngoại lệ với thời gian chờ và thông điệp được cung cấp.
    public RateLimitException(int retryAfterSeconds, String message) {
        super(ErrorCode.ERR_RATE_LIMIT, message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    // Trả về thời gian chờ trước khi thử lại, tính bằng giây.
    public int getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
