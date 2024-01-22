package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmDbStorage storage) {
        this.filmStorage = storage;
    }

    public void addFilm(Film film) {
        filmStorage.add(film);
    }

    public void updateFilm(Film film) {
        hasValidFilmId(film.getId());
        filmStorage.update(film);
    }

    public Film findFilmById(int id) {
        hasValidFilmId(id);
        return filmStorage.read(id);
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public void addLikeToFilm(int filmId, int userId) {
        hasValidFilmId(filmId);
        hasValidUserId(userId);
        filmStorage.addLikeToFilm(filmId, userId);
    }

    public void removeLikeFromFilm(int filmId, int userId) {
        hasValidFilmId(filmId);
        filmStorage.removeLikeFromFilm(filmId, userId);
    }

    public List<Film> findMorePopularFilms(int size) {
        return findAllFilms()
                .stream()
                .sorted((film0, film1) -> {
                    int a = film0.countLikes();
                    int b = film1.countLikes();
                    return Integer.compare(b, a);
                })
                .limit(size)
                .collect(Collectors.toList());
    }

    private boolean hasValidUserId(int id) {
        if (id <= 0) throw new ValidationException(
                String.format("Используется неверный идентификатор пользователя: id=%d", id));
        return true;
    }

    private boolean hasValidFilmId(int id) {
        if (id <= 0) throw new ValidationException(
                String.format("Используется неверный идентификатор фильма: id=%d", id));
        return true;
    }
}
