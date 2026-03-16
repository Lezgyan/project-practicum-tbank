package ru.tbank.practicum.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.tbank.practicum.dto.internal.DtoTimeBlind;

public class TimeBlindValidator implements ConstraintValidator<ValidTimeOpeningAndClosingBlind, DtoTimeBlind> {
    @Override
    public boolean isValid(DtoTimeBlind dtoTimeBlind, ConstraintValidatorContext constraintValidatorContext) {
        if (dtoTimeBlind == null) {
            return true;
        }

        if (dtoTimeBlind.openingTime() == null || dtoTimeBlind.closingTime() == null) {
            return true;
        }

        return dtoTimeBlind.openingTime().isBefore(dtoTimeBlind.closingTime());
    }
}
