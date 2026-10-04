package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

public class RateLimitException extends AppException {

    private final int retryAfterSeconds;

    public RateLimitException() {
        this(0, ErrorCode.ERR_RATE_LIMIT.getDefaultMessage());
    }

    public RateLimitException(String message) {
        this(0, message);
    }

    public RateLimitException(int retryAfterSeconds, String message) {
        super(ErrorCode.ERR_RATE_LIMIT, message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public int getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
