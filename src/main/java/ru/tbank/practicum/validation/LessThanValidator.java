package ru.tbank.practicum.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class LessThanValidator implements ConstraintValidator<LessThan, Object> {
    String lessField;

    String moreField;

    @Override
    public void initialize(LessThan constraintAnnotation) {
        this.lessField = constraintAnnotation.less();
        this.moreField = constraintAnnotation.more();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Comparable<Object> less;
        Comparable<Object> more;

        try {
            less = getComparableProperty(value, lessField);
            more = getComparableProperty(value, moreField);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (less == null || more == null) {
            return true;
        }

        if (less.getClass() != more.getClass()) {
            throw new IllegalStateException("Field classes do not match");
        }

        boolean valid = less.compareTo(more) < 0;

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("%s должен быть больше %s".formatted(moreField, lessField))
                    .addPropertyNode(lessField)
                    .addConstraintViolation();
        }

        return valid;
    }

    private Comparable<Object> getComparableProperty(Object value, String propertyName)
            throws NoSuchFieldException, IllegalAccessException {
        Object property = getProperty(value, propertyName);
        if (property == null) {
            return null;
        }

        if (property instanceof Comparable<?>) {
            return (Comparable<Object>) property;
        }

        throw new IllegalStateException("Поле '%s' должно реализовывать Comparable".formatted(property));
    }

    private Object getProperty(Object value, String propertyName) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(value, propertyName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(value);
        }
        throw new NoSuchFieldException("Field %s not found".formatted(propertyName));
    }

    private Field getField(Object value, String fieldName) {
        try {
            return value.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
