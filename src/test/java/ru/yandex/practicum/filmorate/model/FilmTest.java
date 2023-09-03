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


public class FilmTest {
    private static Validator validator;

    Rating rating = new Rating();
    {
        rating.setId(1);
        rating.setName("G");
    }

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory
                     = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void filmIsValid() {
        Film film = new Film(1, "The Lion King", "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), 88);
        film.setMpa(rating);
        Set<ConstraintViolation<Film>> constraintViolations =
                validator.validate(film);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void checkIncorrectNameField() {
        Film film = new Film(1, " ", "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), 88);
        film.setMpa(rating);
        Set<ConstraintViolation<Film>> constraintViolations =
                validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());

        film = new Film(1, null, "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), 88);
        film.setMpa(rating);
        constraintViolations = validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be blank", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void checkIncorrectDescriptionField() {
        Film film = new Film(1, "The Lion King", "abcd".repeat(50) + "a", LocalDate.of(1994, 6, 24), 88);
        film.setMpa(rating);
        Set<ConstraintViolation<Film>> constraintViolations = validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 0 and 200", constraintViolations.iterator().next().getMessage());
    }

    @Test
    void checkIncorrectReleaseDate() {
        Film film = new Film(1, "The Lion King", "Dramatic story about little lion.", LocalDate.of(1895, 12, 27), 88);
        film.setMpa(rating);
        Set<ConstraintViolation<Film>> constraintViolations = validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("Значение даты должно быть позже \"28.12.1895\".",
                constraintViolations.iterator().next().getMessage());
    }

    @Test
    void checkIncorrectDuration() {
        Film film = new Film(1, "The Lion King", "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), -1);
        film.setMpa(rating);
        Set<ConstraintViolation<Film>> constraintViolations = validator.validate(film);
        assertEquals(1, constraintViolations.size());
        assertEquals("must be greater than or equal to 0", constraintViolations.iterator().next().getMessage());

        film = new Film(1, "The Lion King", "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), 0);
        film.setMpa(rating);
        constraintViolations = validator.validate(film);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void filmLikesTest() {
        Film film = new Film(1, "The Lion King", "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), 88);
        film.setMpa(rating);
        assertEquals(0, film.countLikes());
        film.getLikes().add(1L);
        film.getLikes().add(1L);
        film.getLikes().add(2L);
        assertEquals(2, film.countLikes());
        film.getLikes().remove(1L);
        assertEquals(1, film.countLikes());
    }
}

