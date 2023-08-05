package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private int lastGeneratedFilmId = 0;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage storage) {
        this.filmStorage = storage;
    }

    public void addFilm(Film film) {
        if (film.getId() <= 0) {
            film.setId(++lastGeneratedFilmId);
        }
        filmStorage.add(film);
    }

    public void updateFilm(Film film) {
        if (film.getId() <= 0) throw new ValidationException("Недопустимый id:" + film);
        filmStorage.update(film);
    }

    public Film selectFilmById(int id) {
        return filmStorage.read(id);
    }

    public Collection<Film> selectAllFilms() {
        return filmStorage.selectAllFilms();
    }

    public void addLikeToFilm(int filmId, int userId) {
        selectFilmById(filmId).addLike(userId);
    }

    public void removeLikeFromFilm(int filmId, int userId) {
        selectFilmById(filmId).removeLike(userId);
    }

    public List<Film> selectMorePopularFilms(int size) {
        return selectAllFilms()
                .stream()
                .sorted((film0, film1) -> {
                    int a = film0.countLikes();
                    int b = film1.countLikes();
                    if (a < b) return 1;
                    if (a > b) return -1;
                    return 0;
                })
                .limit(size)
                .collect(Collectors.toList());
    }
}
