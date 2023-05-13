package com.hello.borad.dto.request.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class NullOrEmptyValidator implements ConstraintValidator<NullOrEmpty, List<?>> {

    @Override
    public boolean isValid(List<?> value, ConstraintValidatorContext context) {
        return value == null || value.isEmpty();
    }
}
