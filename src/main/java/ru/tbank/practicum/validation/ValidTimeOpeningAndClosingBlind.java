package ru.tbank.practicum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = TimeBlindValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidTimeOpeningAndClosingBlind {
    String message() default "closingTime должен быть позже openingTime";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
