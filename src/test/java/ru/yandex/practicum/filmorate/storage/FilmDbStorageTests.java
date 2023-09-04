package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {
    private final FilmDbStorage storage;
    private static Rating ratingG = new Rating();
    private static Rating ratingPg = new Rating();
    private static Rating ratingPg13 = new Rating();
    private static Rating ratingR = new Rating();

    static {
        ratingG.setId(1);
        ratingG.setName("G");
        ratingPg.setId(2);
        ratingPg.setName("PG");
        ratingPg13.setId(3);
        ratingPg13.setName("PG-13");
        ratingR.setId(4);
        ratingR.setName("R");
    }

    @Test
    void shouldAddAndReadFilmInStorage() {
        int filmId = 1;
        Film film = new Film(filmId, "The Lion King", "Dramatic story about little lion.",
                LocalDate.of(1994, 6, 24), 88);

        film.setMpa(ratingG);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            storage.read(filmId);
        });
        assertEquals(
                String.format("Фильм id=%d отсутствует в фильмотеке.", film.getId()),
                ex.getMessage());

        storage.add(film);
        Film returnedFilm = storage.read(filmId);
        assertEquals(film, returnedFilm);
        storage.deleteAll();
    }

    @Test
    void shouldCalculateSizeAndDeleteAllFilmsInStorage() {
        int sizeOfStorage = storage.size();
        Film film1 = new Film(1, "The Lion King", "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), 88);
        Film film2 = new Film(2, "The Lion King", "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), 88);
        Film film3 = new Film(3, "The Lion King", "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), 88);

        film1.setMpa(ratingPg);
        film2.setMpa(ratingPg13);
        film3.setMpa(ratingR);

        storage.add(film1);
        storage.add(film2);
        storage.add(film3);

        assertEquals(sizeOfStorage + 3, storage.size());

        storage.deleteAll();
        assertEquals(0, storage.size());
    }

    @Test
    void shouldDeleteFilm() {
        int filmId = 2;
        Film film = new Film(filmId, "The Lion King", "Dramatic story about little lion.",
                LocalDate.of(1994, 6, 24), 88);
        film.setMpa(ratingG);

        storage.deleteAll();
        int sizeBefore = storage.size();
        storage.delete(film);
        assertEquals(sizeBefore, storage.size());

        storage.add(film);
        assertEquals(sizeBefore + 1, storage.size());
        storage.delete(film);
        assertEquals(sizeBefore, storage.size());

        storage.deleteAll();
    }

    @Test
    void shouldSelectAllFilmsFromStorage() {
        assertEquals(0, storage.size());
        Film film1 = new Film(1, "The Lion King", "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), 88);
        Film film2 = new Film(2, "The Lion King", "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), 88);
        Film film3 = new Film(3, "The Lion King", "Dramatic story about little lion.", LocalDate.of(1994, 6, 24), 88);
        film1.setMpa(ratingG);
        film2.setMpa(ratingPg);
        film3.setMpa(ratingR);
        storage.add(film1);
        storage.add(film2);
        storage.add(film3);

        Collection<Film> returned = storage.findAllFilms();
        assertEquals(3, returned.size());
    }
}
