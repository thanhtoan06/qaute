package vn.edu.hcmute.qaute.common.exception;

import java.util.Map;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;

public class BadRequestException extends AppException {

    private final Map<String, String> fieldErrors;

    public BadRequestException() {
        this(ErrorCode.ERR_VALIDATION.getDefaultMessage(), Map.of());
    }

    public BadRequestException(String message) {
        this(message, Map.of());
    }

    public BadRequestException(String message, Map<String, String> fieldErrors) {
        super(ErrorCode.ERR_VALIDATION, message);
        this.fieldErrors = fieldErrors == null ? Map.of() : fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
