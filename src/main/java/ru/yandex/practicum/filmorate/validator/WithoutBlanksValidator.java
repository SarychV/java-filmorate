package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WithoutBlanksValidator implements ConstraintValidator<WithoutBlanks, String> {
    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if (object == null) return true;
        return !object.contains(" ");
    }
}
