package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

public class ConflictException extends AppException {

    public ConflictException() {
        super(ErrorCode.ERR_CONFLICT);
    }

    public ConflictException(String message) {
        super(ErrorCode.ERR_CONFLICT, message);
    }
}
