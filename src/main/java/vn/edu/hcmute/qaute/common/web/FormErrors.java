package vn.edu.hcmute.qaute.common.web;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.validation.BindingResult;

public final class FormErrors {

    private FormErrors() {
    }

    public static Map<String, String> of(BindingResult bindingResult) {
        Map<String, String> errors = new LinkedHashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        bindingResult.getGlobalErrors().forEach(error ->
                errors.putIfAbsent("global", error.getDefaultMessage()));
        return errors;
    }
}
