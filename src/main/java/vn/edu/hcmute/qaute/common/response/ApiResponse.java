package vn.edu.hcmute.qaute.common.response;

import java.util.List;
import org.slf4j.MDC;

// Đóng gói dữ liệu phản hồi API dùng chung qua ApiResponse.
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final List<FieldErrorItem> errors;
    private final String traceId;

    // Khởi tạo phản hồi API cùng mã truy vết hiện tại.
    private ApiResponse(boolean success, String message, T data, List<FieldErrorItem> errors) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.traceId = MDC.get("traceId");
    }

    // Tạo phản hồi thành công chứa dữ liệu.
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, null, data, List.of());
    }

    // Tạo phản hồi thành công với thông điệp và dữ liệu.
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, List.of());
    }

    // Tạo phản hồi thất bại với thông điệp.
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null, List.of());
    }

    // Tạo phản hồi thất bại với thông điệp và danh sách lỗi.
    public static <T> ApiResponse<T> fail(String message, List<FieldErrorItem> errors) {
        return new ApiResponse<>(false, message, null, errors == null ? List.of() : errors);
    }

    // Trả về trạng thái thành công của phản hồi.
    public boolean isSuccess() {
        return success;
    }

    // Trả về thông điệp của phản hồi.
    public String getMessage() {
        return message;
    }

    // Trả về dữ liệu của phản hồi.
    public T getData() {
        return data;
    }

    // Trả về các lỗi trường của phản hồi.
    public List<FieldErrorItem> getErrors() {
        return errors;
    }

    // Trả về mã truy vết của phản hồi.
    public String getTraceId() {
        return traceId;
    }
}
