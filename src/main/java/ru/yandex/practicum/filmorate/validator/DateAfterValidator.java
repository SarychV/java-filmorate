package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateAfterValidator implements ConstraintValidator<DateAfter, LocalDate> {
    private LocalDate checkDate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void initialize(DateAfter constraintAnnotation) {
        this.checkDate = LocalDate.parse(constraintAnnotation.value(), formatter);
    }

    @Override
    public boolean isValid(LocalDate object, ConstraintValidatorContext constraintContext) {
        if (object == null) return false;
        boolean isValid;
        isValid = object.isAfter(checkDate);
        if (!isValid) {
            String errorMessage = String.format("Значение даты должно быть позже \"%1$td.%1$tm.%1$tY\".", checkDate);
            constraintContext.disableDefaultConstraintViolation();
            constraintContext.buildConstraintViolationWithTemplate(errorMessage)
                    .addConstraintViolation();
        }
        return isValid;
    }
}