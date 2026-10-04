package vn.edu.hcmute.qaute.common.exception;

import vn.edu.hcmute.qaute.common.constant.ErrorCode;

public class DuplicateException extends AppException {

    private final String field;

    public DuplicateException() {
        this(null, ErrorCode.ERR_DUPLICATE.getDefaultMessage());
    }

    public DuplicateException(String message) {
        this(null, message);
    }

    public DuplicateException(String field, String message) {
        super(ErrorCode.ERR_DUPLICATE, message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
