package vn.edu.hcmute.qaute.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

// Hỗ trợ kiểm tra hợp lệ cho ValidMssv.
@Documented
@Constraint(validatedBy = MssvValidator.class)
@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidMssv {

    // Cung cấp thông báo mặc định khi MSSV không hợp lệ.
    String message() default "MSSV phải gồm đúng 8 chữ số";

    // Khai báo các nhóm áp dụng ràng buộc này.
    Class<?>[] groups() default {};

    // Khai báo loại dữ liệu bổ sung cho ràng buộc này.
    Class<? extends Payload>[] payload() default {};
}
