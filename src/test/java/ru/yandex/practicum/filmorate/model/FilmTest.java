package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


public class FilmTest {
    Film film;

    @BeforeEach
    public void initFilm() {
        film = new Film(1, "The Lion King", "Dramatic story about little lion.",
                LocalDate.of(1994, 6, 24), 88);
    }

    @Test
    public void filmContainsCorrectFields() {
        Assertions.assertDoesNotThrow(film::validate);
    }

    @Test
    public void checkIncorrectNameField() {
        film = film.toBuilder().name("").build();
        Assertions.assertThrows(ValidationException.class, film::validate);
    }

    @Test
    public void checkIncorrectDescriptionField() {
        StringBuilder descriptionMore200 = new StringBuilder();
        descriptionMore200.append("abcd".repeat(50));
        film = film.toBuilder().description(descriptionMore200.toString()).build();
        Assertions.assertDoesNotThrow(film::validate);
        descriptionMore200.append("defg");
        film = film.toBuilder().description(descriptionMore200.toString()).build();
        Assertions.assertThrows(ValidationException.class, film::validate);
    }

    @Test
    void checkIncorrectReleaseDate() {
        film = film.toBuilder().releaseDate(LocalDate.of(1895, 12, 27)).build();
        Assertions.assertThrows(ValidationException.class, film::validate);
    }

    @Test
    public void checkIncorrectDuration() {
        film = film.toBuilder().duration(-1).build();
        Assertions.assertThrows(ValidationException.class, film::validate);
    }
}
