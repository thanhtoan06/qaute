package vn.edu.hcmute.qaute.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Hỗ trợ kiểm tra hợp lệ cho MssvValidator.
public class MssvValidator implements ConstraintValidator<ValidMssv, String> {

    // Chấp nhận giá trị rỗng hoặc MSSV gồm đúng 8 chữ số.
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.matches("^\\d{8}$");
    }
}
