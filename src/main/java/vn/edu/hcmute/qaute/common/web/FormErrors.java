package vn.edu.hcmute.qaute.common.web;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.validation.BindingResult;

// Hỗ trợ xử lý yêu cầu web dùng chung qua FormErrors.
public final class FormErrors {

    // Ngăn tạo đối tượng từ lớp tiện ích.
    private FormErrors() {
    }

    // Chuyển các lỗi xác thực thành bản đồ tên trường và thông báo lỗi.
    public static Map<String, String> of(BindingResult bindingResult) {
        Map<String, String> errors = new LinkedHashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        bindingResult.getGlobalErrors().forEach(error ->
                errors.putIfAbsent("global", error.getDefaultMessage()));
        return errors;
    }
}
