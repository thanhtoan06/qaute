package vn.edu.hcmute.qaute.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Hỗ trợ kiểm tra hợp lệ cho StrongPasswordValidator.
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    // Kiểm tra độ dài và thành phần bắt buộc của mật khẩu.
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() < 8 || value.length() > 64) {
            return false;
        }
        return value.chars().anyMatch(Character::isUpperCase)
                && value.chars().anyMatch(Character::isLowerCase)
                && value.chars().anyMatch(Character::isDigit)
                && value.chars().anyMatch(character -> !Character.isLetterOrDigit(character));
    }
}
