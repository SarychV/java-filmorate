package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryFilmStorageTest {
    FilmStorage storage = new InMemoryFilmStorage();

    @Test
    void shouldAddAndReadFilmInStorage() {
        int filmId = 1;
        Film film = new Film(filmId, "The Lion King", "Dramatic story about little lion.",
                LocalDate.of(1994, 6, 24), 88);

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
        storage.add(film1);
        storage.add(film2);
        storage.add(film3);

        Collection<Film> returned = storage.selectAllFilms();
        assertEquals(3, returned.size());
    }
}
