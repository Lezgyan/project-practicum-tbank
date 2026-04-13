package ru.tbank.practicum.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LessThanValidatorTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void isValid_lessFieldIsLessThanMoreField_returnsTrue() {
        TestRange dto = new TestRange(3, 10);

        Set<ConstraintViolation<TestRange>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    public void isValid_lessFieldIsEqualToMoreField_returnsFalse() {
        TestRange dto = new TestRange(5, 5);

        Set<ConstraintViolation<TestRange>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        ConstraintViolation<TestRange> violation = violations.iterator().next();

        assertEquals("maxValue должен быть больше minValue", violation.getMessage());
        assertEquals("minValue", violation.getPropertyPath().toString());
    }

    @Test
    public void isValid_lessFieldIsGreaterThanMoreField_returnsFalse() {
        TestRange dto = new TestRange(10, 3);

        Set<ConstraintViolation<TestRange>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        ConstraintViolation<TestRange> violation = violations.iterator().next();

        assertEquals("maxValue должен быть больше minValue", violation.getMessage());
        assertEquals("minValue", violation.getPropertyPath().toString());
    }

    @Test
    public void isValid_lessFieldIsNull_returnsTrue() {
        TestRange dto = new TestRange(null, 5);

        Set<ConstraintViolation<TestRange>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    public void isValid_moreFieldIsNull_returnsTrue() {
        TestRange dto = new TestRange(5, null);

        Set<ConstraintViolation<TestRange>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void valid_whenBothFieldsAreNull() {
        TestRange dto = new TestRange(null, null);

        Set<ConstraintViolation<TestRange>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    public void isValid_fieldClassesDoNotMatch_throwsIllegalStateException() {
        MixedTypes dto = new MixedTypes(1, 5L);

        assertThatThrownBy(() -> validator.validate(dto))
                .hasCauseInstanceOf(IllegalStateException.class)
                .rootCause()
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Field classes do not match");
    }

    @LessThan(less = "minValue", more = "maxValue")
    private static class TestRange {
        private final Integer minValue;
        private final Integer maxValue;

        private TestRange(Integer minValue, Integer maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }
    }

    @LessThan(less = "minValue", more = "maxValue")
    private static class MixedTypes {
        private final Integer minValue;
        private final Long maxValue;

        private MixedTypes(Integer minValue, Long maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }
    }
}
