package vn.edu.hcmute.qaute.common.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.RateLimitException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.common.response.FieldErrorItem;
import vn.edu.hcmute.qaute.common.web.FormErrors;

// Chuyển ngoại lệ ứng dụng thành phản hồi phù hợp qua GlobalExceptionHandler.
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Xử lý ngoại lệ nghiệp vụ và tạo phản hồi phù hợp với yêu cầu.
    @ExceptionHandler(AppException.class)
    public Object handleAppException(AppException exception, HttpServletRequest request) {
        ErrorCode code = exception.getCode();
        List<FieldErrorItem> errors = exception instanceof BadRequestException badRequest
                ? badRequest.getFieldErrors().entrySet().stream()
                .map(entry -> new FieldErrorItem(entry.getKey(), entry.getValue())).toList()
                : List.of();
        HttpHeaders headers = new HttpHeaders();
        if (exception instanceof RateLimitException rateLimit) {
            headers.set(HttpHeaders.RETRY_AFTER, String.valueOf(rateLimit.getRetryAfterSeconds()));
        }
        Integer retryAfter = exception instanceof RateLimitException rateLimit
                ? rateLimit.getRetryAfterSeconds() : null;
        return respond(request, code, exception.getMessage(), errors, headers, retryAfter,
                HttpStatus.valueOf(code.getHttpStatus()));
    }

    // Chuyển lỗi kiểm tra dữ liệu thành danh sách lỗi trường.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldErrorItem> errors = FormErrors.of(bindingResult).entrySet().stream()
                .map(entry -> new FieldErrorItem(entry.getKey(), entry.getValue())).toList();
        return respond(request, ErrorCode.ERR_VALIDATION, ErrorCode.ERR_VALIDATION.getDefaultMessage(),
                errors, new HttpHeaders());
    }

    // Trả về lỗi khi tệp tải lên vượt quá kích thước cho phép.
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Object handleMaxUpload(MaxUploadSizeExceededException exception, HttpServletRequest request) {
        return respond(request, ErrorCode.ERR_FILE_REJECTED,
                ErrorCode.ERR_FILE_REJECTED.getDefaultMessage(), List.of(), new HttpHeaders());
    }

    // Trả về lỗi không tìm thấy tài nguyên hoặc trình xử lý.
    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public Object handleNotFound(Exception exception, HttpServletRequest request) {
        return respond(request, ErrorCode.ERR_NOT_FOUND, ErrorCode.ERR_NOT_FOUND.getDefaultMessage(),
                List.of(), new HttpHeaders());
    }

    // Trả về lỗi khi phương thức HTTP không được hỗ trợ.
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleBadRequest(Exception exception, HttpServletRequest request) {
        return respond(request, ErrorCode.ERR_VALIDATION, ErrorCode.ERR_VALIDATION.getDefaultMessage(),
                List.of(), new HttpHeaders());
    }

    // Xử lý ngoại lệ chưa được phân loại và ghi nhận mã truy vết.
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception exception, HttpServletRequest request) {
        String traceId = traceId();
        LOG.error("Lỗi không xử lý được, traceId={}", traceId, exception);
        return respond(request, ErrorCode.ERR_CONFLICT,
                "Có lỗi hệ thống, mã tham chiếu " + traceId, List.of(), new HttpHeaders(),
                null,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Tạo phản hồi với trạng thái mặc định từ mã lỗi.
    private Object respond(HttpServletRequest request, ErrorCode code, String message,
                           List<FieldErrorItem> errors, HttpHeaders headers) {
        return respond(request, code, message, errors, headers, null,
                HttpStatus.valueOf(code.getHttpStatus()));
    }

    // Tạo phản hồi API hoặc trang lỗi với trạng thái và thông tin bổ sung.
    private Object respond(HttpServletRequest request, ErrorCode code, String message,
                           List<FieldErrorItem> errors, HttpHeaders headers, Integer retryAfter,
                           HttpStatus status) {
        if (isApi(request)) {
            ApiResponse<Void> body = errors.isEmpty() ? ApiResponse.fail(message) : ApiResponse.fail(message, errors);
            return new ResponseEntity<>(body, headers, status);
        }
        ModelAndView modelAndView = new ModelAndView(viewFor(status.value()));
        modelAndView.setStatus(status);
        modelAndView.addObject("message", message);
        modelAndView.addObject("traceId", traceId());
        if (retryAfter != null) {
            modelAndView.addObject("retryAfter", retryAfter);
        }
        return modelAndView;
    }

    // Kiểm tra yêu cầu có cần nhận phản hồi API hay không.
    private boolean isApi(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String requestedWith = request.getHeader("X-Requested-With");
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        return (uri != null && uri.startsWith("/api/"))
                || "XMLHttpRequest".equalsIgnoreCase(requestedWith)
                || (accept != null && accept.contains("application/json"));
    }

    // Chọn trang lỗi tương ứng với mã trạng thái HTTP.
    private String viewFor(int status) {
        return switch (status) {
            case 400 -> "error/400";
            case 401 -> "error/401";
            case 403 -> "error/403";
            case 404 -> "error/404";
            case 429 -> "error/429";
            default -> "error/500";
        };
    }

    // Lấy mã truy vết hiện tại hoặc giá trị thay thế.
    private String traceId() {
        String traceId = MDC.get("traceId");
        return traceId == null ? "unknown" : traceId;
    }
}
