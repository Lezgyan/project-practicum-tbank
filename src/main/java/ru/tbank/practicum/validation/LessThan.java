package ru.tbank.practicum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LessThanValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LessThan {

    String less();

    String more();

    String message() default "More должен быть больше less";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
