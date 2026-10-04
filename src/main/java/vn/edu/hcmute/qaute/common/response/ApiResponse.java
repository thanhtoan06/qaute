package vn.edu.hcmute.qaute.common.response;

import java.util.List;
import org.slf4j.MDC;

public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final List<FieldErrorItem> errors;
    private final String traceId;

    private ApiResponse(boolean success, String message, T data, List<FieldErrorItem> errors) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.traceId = MDC.get("traceId");
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, null, data, List.of());
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, List.of());
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null, List.of());
    }

    public static <T> ApiResponse<T> fail(String message, List<FieldErrorItem> errors) {
        return new ApiResponse<>(false, message, null, errors == null ? List.of() : errors);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public List<FieldErrorItem> getErrors() {
        return errors;
    }

    public String getTraceId() {
        return traceId;
    }
}
