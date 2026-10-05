package vn.edu.hcmute.qaute.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MssvValidator implements ConstraintValidator<ValidMssv, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.matches("^\\d{8}$");
    }
}
