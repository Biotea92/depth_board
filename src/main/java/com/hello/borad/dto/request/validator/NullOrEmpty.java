package com.hello.borad.dto.request.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrEmptyValidator.class)
@Documented
public @interface NullOrEmpty {

    String message() default "{nullOrEmpty}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
