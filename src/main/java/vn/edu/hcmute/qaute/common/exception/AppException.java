package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

public class AppException extends RuntimeException {

    private final ErrorCode code;

    public AppException(ErrorCode code) {
        this(code, code.getDefaultMessage());
    }

    public AppException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}
