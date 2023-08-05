package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory
                     = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void userIsValid() {
        User user = new User(1, "user@localhost", "user1", "user#1", LocalDate.of(2002,03,19));
        Set<ConstraintViolation<User>> constraintViolations =
                validator.validate(user);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void checkIncorrectEmailValues() {
        User user = new User(1, "postbox", "user1", "user#1", LocalDate.of(2002,03,19));
        Set<ConstraintViolation<User>> constraintViolations =
                validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("must be a well-formed email address", constraintViolations.iterator().next().getMessage());
        user = new User(1, null, "user1", "user#1", LocalDate.of(2002,03,19));
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void checkIncorrectLoginValues() {
        User user = new User(1, "user@localhost", null, "user#1", LocalDate.of(2002,03,19));

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(2, constraintViolations.size());
        assertEquals("must not be empty", constraintViolations.iterator().next().getMessage());

        user = new User(1, "user@localhost", "", "user#1", LocalDate.of(2002,03,19));
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be empty", constraintViolations.iterator().next().getMessage());

        user = new User(1, "user@localhost", " user", "user#1", LocalDate.of(2002,03,19));
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("Строка содержит пробелы.", constraintViolations.iterator().next().getMessage());

        user = new User(1, "user@localhost", "user ", "user#1", LocalDate.of(2002,03,19));
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("Строка содержит пробелы.", constraintViolations.iterator().next().getMessage());

        user = new User(1, "user@localhost", "us er", "user#1", LocalDate.of(2002,03,19));
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("Строка содержит пробелы.", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void checkIncorrectBirthdayValues() {
        User user = new User(1, "user@localhost", "user", "user#1", LocalDate.of(2002,03,19));
        user.setBirthday(LocalDate.now());
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(0, constraintViolations.size());

        user.setBirthday(LocalDate.now().plusDays(1));
        constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());
        assertEquals("must be a date in the past or in the present",
                constraintViolations.iterator().next().getMessage());
    }
}
