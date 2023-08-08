package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage storage) {
        this.filmStorage = storage;
    }

    public void addFilm(Film film) {
        filmStorage.add(film);
    }

    public void updateFilm(Film film) {
        filmStorage.update(film);
    }

    public Film findFilmById(int id) {
        return filmStorage.read(id);
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.selectAllFilms();
    }

    public void addLikeToFilm(int filmId, int userId) {
        findFilmById(filmId).addLike(userId);
    }

    public void removeLikeFromFilm(int filmId, int userId) {
        findFilmById(filmId).removeLike(userId);
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
}
