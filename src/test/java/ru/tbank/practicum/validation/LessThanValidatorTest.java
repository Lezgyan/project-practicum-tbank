package ru.tbank.practicum.validation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LessThanValidatorTest {

    private LessThanValidator validator;

    @Mock
    private LessThan lessThan;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

    @BeforeEach
    void setUp() {
        validator = new LessThanValidator();

        when(lessThan.less()).thenReturn("minValue");
        when(lessThan.more()).thenReturn("maxValue");

        validator.initialize(lessThan);
    }

    @Test
    void isValid_valueIsNull_returnsTrue() {
        boolean result = validator.isValid(null, context);

        assertTrue(result);
    }

    @Test
    void isValid_lessFieldIsLessThanMoreField_returnsTrue() {
        TestRange value = new TestRange(3, 10);

        boolean result = validator.isValid(value, context);

        assertTrue(result);
    }

    @Test
    void isValid_lessFieldIsEqualToMoreField_returnsFalse() {
        TestRange value = new TestRange(5, 5);

        when(context.buildConstraintViolationWithTemplate("maxValue должен быть больше minValue"))
                .thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode("minValue")).thenReturn(nodeBuilder);

        boolean result = validator.isValid(value, context);

        assertFalse(result);
    }

    @Test
    void isValid_lessFieldIsGreaterThanMoreField_returnsFalse() {
        TestRange value = new TestRange(10, 3);

        when(context.buildConstraintViolationWithTemplate("maxValue должен быть больше minValue"))
                .thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode("minValue")).thenReturn(nodeBuilder);

        boolean result = validator.isValid(value, context);

        assertFalse(result);
    }

    @Test
    void isValid_lessFieldIsNull_returnsTrue() {
        TestRange value = new TestRange(null, 5);

        boolean result = validator.isValid(value, context);

        assertTrue(result);
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    void isValid_moreFieldIsNull_returnsTrue() {
        TestRange value = new TestRange(5, null);

        boolean result = validator.isValid(value, context);

        assertTrue(result);
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    void isValid_fieldClassesDoNotMatch_throwsIllegalStateException() {
        MixedTypes value = new MixedTypes(1, 5L);

        assertThatThrownBy(() -> validator.isValid(value, context))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Field classes do not match");
    }

    private static class TestRange {
        private final Integer minValue;
        private final Integer maxValue;

        private TestRange(Integer minValue, Integer maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }
    }

    private static class MixedTypes {
        private final Integer minValue;
        private final Long maxValue;

        private MixedTypes(Integer minValue, Long maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }
    }
}
