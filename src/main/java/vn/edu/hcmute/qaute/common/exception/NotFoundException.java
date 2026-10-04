package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

public class NotFoundException extends AppException {

    public NotFoundException() {
        super(ErrorCode.ERR_NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(ErrorCode.ERR_NOT_FOUND, message);
    }
}
