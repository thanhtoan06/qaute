package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

public class ForbiddenException extends AppException {

    public ForbiddenException() {
        super(ErrorCode.ERR_FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(ErrorCode.ERR_FORBIDDEN, message);
    }
}
