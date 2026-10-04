package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

public class InvalidTransitionException extends AppException {

    public InvalidTransitionException() {
        super(ErrorCode.ERR_INVALID_TRANSITION);
    }

    public InvalidTransitionException(String message) {
        super(ErrorCode.ERR_INVALID_TRANSITION, message);
    }
}
